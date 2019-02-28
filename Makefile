OCAML=ocamlc
MLFLAGS=-thread

C=gcc
CFLAGS= -g -lm

SRC_SERVER=src/server/
SRC_CLIENT=src/client/
OBJDIR=bin/

TARGET_SERVER=Server
TARGET_CLIENT=Client

# OCaml

# sort interfaces by dependencies
INTERFACES=$(wildcard $(SRC_SERVER)*.mli)
OBJSI=$(patsubst $(SRC_SERVER)%.mli,$(OBJDIR)%.cmi,$(INTERFACES))
OBJSO=$(patsubst $(SRC_SERVER)%.mli,$(OBJDIR)%.cmo,$(INTERFACES))

# C

HEADERS=$(wildcard $(SRC_CLIENT)*.h)
OBJSC=$(patsubst $(SRC_CLIENT)%.h,$(OBJDIR)%.o,$(HEADERS))

all: $(OBJSI) $(OBJSO) $(TARGET_SERVER) $(OBJSC) $(TARGET_CLIENT)

# OCaml

$(OBJDIR)%.cmi: $(SRC_SERVER)%.mli
	$(OCAML) -I $(OBJDIR) -c $< $(MLFLAGS) -o $@

$(OBJDIR)%.cmo: $(SRC_SERVER)%.ml $(OBJDIR)%.cmi
	$(OCAML) -I $(OBJDIR) -c $< $(MLFLAGS) -o $@

$(TARGET_SERVER): $(OBJSO)
	$(OCAML) -I $(OBJDIR) $^ $(MLFLAGS) -o $@

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
