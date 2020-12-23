import os
for i in range(8):
    f = open("empty_bookshelf_%d.json"%(i), "w")
    f.write('''{
	"parent": "block/cube_column",
    "textures": {
        "end": "block/oak_planks",
		"side": "cpp:block/empty_bookshelf_%d"
    }
}'''%(i))
    f.close()
