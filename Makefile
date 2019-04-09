OCAML=ocamlc
MLFLAGS=-thread unix.cma threads.cma

JAVA=javac
JAVAFLAGS=-Xlint

SRC_SERVER=src/server/
SRC_CLIENT=src/client/
OBJDIR=bin/

TARGET_SERVER=Server
TARGET_CLIENT=Client

# OCaml

# sort interfaces by dependencies
INTERFACES=Values.mli Object.mli Command.mli Arena.mli Player.mli Server.mli
OBJSI=$(patsubst %.mli,$(OBJDIR)%.cmi,$(INTERFACES))
OBJSO=$(patsubst %.mli,$(OBJDIR)%.cmo,$(INTERFACES))

all: $(OBJSI) $(OBJSO) $(TARGET_SERVER) $(TARGET_CLIENT)

# OCaml

$(OBJDIR)%.cmi: $(SRC_SERVER)%.mli
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -c $< -o $@

$(OBJDIR)%.cmo: $(SRC_SERVER)%.ml $(OBJDIR)%.cmi
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -c $< -o $@

$(TARGET_SERVER): $(OBJSO)
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -I $(SRC_SERVER) $^ -o $@

# Java

$(TARGET_CLIENT):
	$(JAVA) $(JAVAFLAGS) -d $(OBJDIR) $(SRC_CLIENT)client/*.java $(SRC_CLIENT)model/*.java $(SRC_CLIENT)view/*.java

clean:
	rm -f $(OBJSI)
	rm -f $(OBJSO)
	rm -f $(TARGET_SERVER)
