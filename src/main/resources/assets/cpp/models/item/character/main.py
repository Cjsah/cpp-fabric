json = """{{
\t"parent": "cpp:item/character",
\t"textures": {{
\t\t"layer0": "cpp:character/{}"
\t}}
}}"""

for i in range(1, 118):
    with open("{}.json".format(i), "w+")as f:
        f.write(json.format(i))
