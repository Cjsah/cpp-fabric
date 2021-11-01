import os

json = """{{
\t"parent": "cpp:item/character",
\t"textures": {{
\t\t"layer0": "cpp:character/{}"
\t}}
}}"""

for i in range(1, 130+1):
    name = "{}.json".format(i)
    if not os.path.exists(name):
        with open(name, "w+")as f:
            f.write(json.format(i))
