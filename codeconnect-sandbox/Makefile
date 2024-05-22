OUT_DIR=./out
SRC_DIR=./src
TMP_DIR=./tmp

MKDIRS += out
MKDIRS += tmp

CFLAGS= -Wall -pthread

ifdef DEBUG
CFLAGS += -g -DDEBUG
else
CFLAGS += -O3
endif

_HEADERS = constants.h sandbox.h argv.h diff.h utils.h log.h
HEADERS = $(patsubst %,$(SRC_DIR)/%,$(_HEADERS))

_OBJ = main.o constants.o sandbox.o argv.o diff.o utils.o log.o
OBJ = $(patsubst %,$(OUT_DIR)/%,$(_OBJ))

RELEASE := $(shell cat /etc/os-release | grep NAME | awk 'NR==1' | cut -d '=' -f 2 | sed 's/\"//g')
ifeq ($(RELEASE), Alpine Linux)
OBJ += /usr/lib/libargp.a
endif

$(OUT_DIR)/%.o: $(SRC_DIR)/%.c $(HEADERS) | $(OUT_DIR)
	$(CC) $(CFLAGS) -c -o $@ $<

runner: $(OBJ)
	$(CC) $(CFLAGS) -o $@ $^

.PHONY: clean
clean:
	rm -rf $(OUT_DIR)/*.o $(SRC_DIR)/*~ $(TMP_DIR)/*.o *~
	rm -rf main runner
	rm -rf *.log *.out

C_BASE=./tests/c
NODE_BASE=./tests/node

c: $(C_BASE)/main.c
	$(CC) $< -o main

testc: c runner | $(TMP_DIR)
	sudo ./runner -l $(TMP_DIR)/c.log -t 1000 -m 2048 -i $(C_BASE)/1.in -o $(C_BASE)/1.out -u $(TMP_DIR)/c.tmp.out ./main

testnode: runner | $(TMP_DIR)
	sudo ./runner -l $(TMP_DIR)/node.log -t 1000 -m 2048 --mco -i $(NODE_BASE)/1.in -o $(NODE_BASE)/1.out -u $(TMP_DIR)/node.tmp.out -- node $(NODE_BASE)/main.js

$(sort $(MKDIRS)):
	mkdir -p $@
