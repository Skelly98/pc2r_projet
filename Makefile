OCAML=ocamlc
MLFLAGS=-thread unix.cma threads.cma

C=gcc
CFLAGS= -g -lm

SRC_SERVER=src/server/
SRC_CLIENT=src/client/
OBJDIR=bin/

TARGET_SERVER=Server
TARGET_CLIENT=Client

# OCaml

# sort interfaces by dependencies
INTERFACES=Constants.mli Object.mli Command.mli Arena.mli Server.mli
OBJSI=$(patsubst %.mli,$(OBJDIR)%.cmi,$(INTERFACES))
OBJSO=$(patsubst %.mli,$(OBJDIR)%.cmo,$(INTERFACES))

# C

HEADERS=$(wildcard $(SRC_CLIENT)*.h)
OBJSC=$(patsubst $(SRC_CLIENT)%.h,$(OBJDIR)%.o,$(HEADERS))

all: $(OBJSI) $(OBJSO) $(TARGET_SERVER) $(OBJSC) $(TARGET_CLIENT)

# OCaml

$(OBJDIR)%.cmi: $(SRC_SERVER)%.mli
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -c $< -o $@

$(OBJDIR)%.cmo: $(SRC_SERVER)%.ml $(OBJDIR)%.cmi
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -c $< -o $@

$(TARGET_SERVER): $(OBJSO)
	$(OCAML) $(MLFLAGS) -I $(OBJDIR) -I $(SRC_SERVER) $^ -o $@

# C

$(OBJDIR)%.o: $(SRC_CLIENT)%.c $(SRC_CLIENT)%.h
	$(C) -I $(OBJDIR) -o $@ -c $< $(CFLAGS)

$(TARGET_CLIENT): $(OBJSC) $(SRC_CLIENT)$(TARGET_CLIENT).c
	$(C) -I $(OBJDIR) -o $@ $^ $(CFLAGS)

clean:
	rm -f $(OBJSI)
	rm -f $(OBJSO)
	rm -f $(OBJSC)
	rm -f $(TARGET_SERVER)
	rm -f $(TARGET_CLIENT)
