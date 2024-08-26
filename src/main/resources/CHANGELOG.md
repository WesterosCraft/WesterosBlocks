# 21 Jun 2024:
- Added additional polished stone variants.
- Added CTM and connectstate to polished stone, allowing for creation of arbitrary block sizes using data cycler tool.
- Added house banners, generic wool banners, and updates to existing banners.
- Added additional engraved ashlar colors.
- Added scorched earth layers.
- Added sandy pink smooth ashlar and plaster.
- Added warm pink plaster.
- Added umber and light umber plaster and timber frames.
- Added harrenhal secret door.
- Added black melted ashlar blocks and "bricicles" for harrenhal.
- Added "symmetrical" blockstate to left/righthatch timber frame blocks, allowing for better corner patterns.
- Added scarred weirwood block.
- Added green-veined black harrenhal marble.
- Added bushy grass blocks to replace uses of leaf blocks in ground.
- Added coral, seagrass, and kelp blocks.
- Added cheval de frise.
- Added custom model for flat wheel as well as new large wheel.
- Changed locked fence gates to allow opening by clicking with empty hand while in creative mode.
- Changed betterfoliage leaf models.
- Changed butterfly and dragonfly textures.
- Fixed bug with red keep secret door texture.
- Fixed bug with empty cabinet CTM.
- Fixed bug with pale dark red medium ashlar.
- Fixed issue with signs not being placeable on cloth material blocks.
- Fixed wrong biome colors in desert biome.
- Fixed transparency issues with fruit leaves and dappled moss.

# 22 Jan 2024:
- Added a large number of new stone brick and cobblestone sets to fill in gaps in existing block sets, and to remove biome dependencies.
- Added "smooth" and "rough" plaster blocks and variants, retexturing existing plaster blocks as appropriate.
- Added new timber frame CTM variants (bressummer, left/righthatch large, left/righthatch studded), and added new CTM to existing crosshatch/frame blocks.
- Added mullion window frames.
- Added volcanic pebbles and sand.
- Added dragonfly and butterfly blocks.
- Added "grassy dirt" and "thick grass" blocks to replace forest biome CTMs for dirt/grass.
- Added southern brick block with rough reused brick, dark mortar brick, lintel, arch, and flat arch variants.
- Added additional orange brick variants: dark mortar bricks, rowlock course, dentil course, single arch, and double arch.
- Added unlit torch block, unlit candle block (non-cuboid), and unlit blockstates for candle altar and lantern blocks.
- Added blockstates to random blocks to allow control over textures: plates, paper, clothesline, bones, cobweb, smoke, coins, profession signs.
- Added birch, jungle, northern, grey, and white wood doors and shutters.
- Added vertical planks and window frame blocks for all colored woods.
- Added orange wood and variants.
- Added dead jungle tall grass and savanna tall grass blocks.
- Added new terrainsets: alts for vale/east/iron/north, a darker alt for westerlands, red granite, dornish marches + alt, and gray granite + alt (replacement for biome shaded eastern island).
- Added pebbles for new red granite, dornish marches, and brown granite sets.
- Added more furnace, faith carving, and engraving variants.
- Added generic version of iron grate/sewer manhole blocks.
- Added night's watch banner.
- Changed small orange bricks to use a new brick texture, and orange bricks to use a new rough reused brick texture.
- Changed timber frame blocks to use new plaster and timber textures.
- Changed small orange brick to new light mortar orange brick texture, and orange brick to a rough "reused" brick texture.
- Changed light stone textures to a new smooth ashlar texture.
- Changed iron rungs to custom model and added new broken iron rungs block.
- Changed the large paintings to use a tapestry-like texture.
- Changed cuboid unlit candle block to use new model/texture to fix transparent pixels.
- Changed vanilla campfire block to custom texture.
- Updated particle textures.
- Fixed candle/torch blocks not being placeable on non-solid surfaces and moved them to correct inventory tab.
- Fixed cow parsley and fireweed CTMs.
- Fixed bottom texture for clay pot blocks (to oak planks, so that they can be used in shelves).
- Fixed wrong red lady 2x2 painting texture.
- Fixed bug with red sandstone slab.
- Fixed rendering conflicts w/ Embeddium for blood, table, bread, cheese, fish trap, and cage blocks.
- Reorganized stone brick and cobblestone sets, using the format "<texture type> <set name>" (e.g., "Half Ashlar Dark Grey").

# 2 Jan 2024:
- Refactored WesterosBlocks.json into "block sets" that are used to auto-generate standard variants (e.g. stairs, slabs, walls, etc.);
 several blocks got additional variants in this process.
- Added directional firewood slab.
- Added vanilla vines as custom block.
- Added slabs and layers for muddy sand, grassy sand, and ash.
- Added variants for wool blocks.
- Fixed slate stairs missing uvlock.
- Fixed northern biome CTM for grassy sand.

# 10 October 2023:
- Officially migrated WesterosBlocks to minecraft 1.18. The structural changes to the modpack are too vast to fully note here, but some of the block changes are listed below:
- Migrated many vanilla blocks to custom blocks in order to ensure that remaining vanilla textures resemble the underlying block meaning.
- Added potted plant blocks for all types of flowers/grasses.
- Added a "layer" blockstate to plant and web blocks to allow them to be placed on top of layers.
- Changed vanilla grass to have the grass texture on all sides; added "classic grass" as a custom block.

# 13 November 2021:
- Added stormlands large brick variants
- Added variants for remaining wood colors
- Added light/dark versions of wattle fence
- Added ghostskin
- Added hemp blocks (short/tall/dense variants)
- Added more variants of faith carved stone
- Added stacked & piled bone blocks & variants
- Added firewood slab
- Added path blocks for dorne dusty dirt, dorne dry soil, and reach gravel
- Added slab block for dorne dry soil
- Added arbor brick furnace blocks
- Added black glass candle variant
- Added variants for snow/ice blocks (note: the latter have some minor rendering issues that will be fixed in the future)
- Added snowy versions of dark/light thatch
- Added dock leaf block and removed texture from nettle CTM
- Added snowy weirwood leaves
- Added domestic/workshop/shop/yard utility blocks
- Added wool/thatch carpets as custom blocks
- Added more arrow slit options
- Added more hopper options
- Added stair/slab/wall/fence variants for light oldtown brick
- Added pale pink bricks & variants for Maidenpool
- Added stair/slab/wall/fence for (icy) northern cobble, faint light grey stone, small orange brick, white granite, and small white brick
- Added great road etchings
- Added northern fence gate (on vanilla dark oak fence gate block)
- Added orange slate for Arbor/Oldtown
- Added wood and metal variants of tip blocks
- Added variants for iron, steel, and gold
- Added variants for arbor small brick
- Changed furnace blocks to have hollow models (& added vanilla unlit stone brick furnace as custom block)
- Changed terrainset, ores, and cushion moss to have overlay CTMs
- Changed steel/iron blocks to have similar texture to bronze/gold
- Changed grass/foliage shading in desert biome to yellower variant of mushroom_island (tentative)
- Changed shading of grey bricks in jungle biome to browner hue
- Changed current pink brick blocks to sandy pink textures that work with vivid dark sandstone
- Changed arbor brick texture to be more similar to dun brick
- Changed ash/scorched earth blocks to have top texture on all sides
- Changed dirt texture in jungle biome to not have moss
- Changed grassy sand to have proper colors in extreme_hills biome
- Changed iron nugget item texture to folding fan
- Changed stormlands & white harbor blocks to have generic names
- Fixed bug with jungle biome shading and height
- Fixed oak timber white daub lefthatch/righthatch having wrong inventory icons
- Fixed wattle fence having transparent inventory icon

# 10 September 2020:
- Added vine model versions of falling water blocks
- Added renly banner
- Added slabs/stairs/wall for cushion moss
- Added yellow daub block to match new northern yellow texture
- Changed color of Redwyne banner
- Changed all timber & wattle blocks to have wood top
- Changed name of "Clay" to "Red Clay Plaster"
- Changed whitewashed timber to be more gray, smoothed infill
- Changed northern yellow timber block to have paler infill
- Changed white wood planks to match whitewashed timber & vertical planks better
- Changed timber window frames to have filled in corners
- Fixed dappled moss accidentally being named jasmine vines

# 7 September 2020:
- Added vivid dark sandstone engraved stone and arrow slits
- Added red fork pink sandstone/brick variants
- Added timber window frames
- Added redwyne & grafton banners
- Added valyrian stone wall
- Added dirt/gravel forest paths
- Added wood covers as custom blocks for each standard plank variant
- Added northern d&w variants (white, brown, orange brick, yellow, red, grey brick, blue, green)
- Added oak d&w variants (yellow, orange, lannisport brick)
- Added whitewashed d&w
- Added vertical d&w for all existing variants
- Added close-studding d&w for primary sets (white/brown/orange brick) + whitewashed
- Added vine-like dappled moss block
- Added cushion moss
- Added flowstone
- Added volcanic rock
- Added grey sandstone terrainset
- Added alt terrainsets for reach, westerlands, red mountains, dorne, stormlands
- Changed river cobble to have random CTM
- Changed pink thistle to have 2-high CTM
- Changed blue slate to be a paler, grey-ish blue
- Changed ore blocks to have new textures
- Changed eastern island, iron island terrainsets to remove moss
- Changed reach, westerlands, dorne, river terrainsets to have better textures
- Changed jasmine vine to use vine block model (old ID still exists but is deprecated)
- Fixed new mob textures changing skull blocks

# 20 June 2020:
- Changed mob textures to CashBanks textures
- Fixed vanilla bed texture

# 26 May 2020:
- Added stairs/slabs/walls/fences for terrainset blocks

# 15 May 2020:
- Added stairs/slabs/walls for river cobble & small smooth stone brick
- Added arbor small brick
- Added normal & ornate arrow slits for small orange brick
- Added snowy spruce leaves block
- Changed terrainset textures
- Changed ocean pebble texture to match new terrainset
- Changed arbor brick to have less contrast and no border
- Fixed leaves biome shading by adding custom betterfoliage effect
- Fixed stair textures bug
- Fixed discoloured snow tile
- Fixed bed bottom textures

# 4 October 2019:
- Changed coin block to have more varied shapes and types
- Changed Brown Mushroom 11 and 12 back to original textures
- Changed short wattle fence texture to get rid of spikes
- Changed furnace blocks to have small ledge on bottom
- Fixed cranberry CTM being broken

# 21 September 2019:
- Added short (non-ctm) wattle fence
- Added stairs, slabs, vertical planks, etc. for grey & white wood
- Added stairs, slabs, etc. for reach cobblestone
- Added window & ornate arrow slit versions for dun brick
- Added grassy sand
- Added muddy sand
- Added peat
- Added duckweed
- Added white & pink roses to match leaf blocks
- Added heather block
- Added red fern block
- Changed snow blocks to have less shading on the sides (and smooth lighting disabled)
- Changed mushrooms to have better textures
- Changed red keep door to use coarse red sandstone
- Changed gray wood block to be slightly more faded
- Changed all flower blocks to have real names
- Changed cranberry texture to be less plastic-looking
- Changed bracken & dead bracken to have additional textures in mix
- Changed nettle block to have 2-high ctm
- Changed sunflower to have 2-high ctm
- Changed red flower 23 to have some random ctm
- Changed red flower 19 to be alpine sow thistle
- Changed red flower 11 to be blue chicory
- Changed red flower 2 to meadow fescue block
- Changed yellow flower 15, 26, 27 so that the base matches grass color
- Changed yellow flower 19 to be bog asphodel
- Changed yellow flower 14 to be galium verum/mustard
- Changed yellow flower 13 to have better dead grass texture
- Changed yellow flower 12 to have better texture + random ctm
- Changed yellow flower 10 texture slightly
- Changed yellow flower 2 to have some random ctm

# 16 January 2019:
- Added two more weirwood faces
- Added industry utility block
- Added slab, stairs, wall, fence, hopper of Oldtown cobble
- Added Lannisport slate tip
- Added Lannisport light brick arrow slit window
- Added custom carpet blocks for all wood types
- Added slab, stairs, wall for Mances Camp Common Tent 2 and Mances Camp Mance's Tent
- Changed color of Mances Camp tent insides to match dark thatch
- Changed Bolton banner to have updated texture
- Changed Oldtown sewer manhole to not have "OT" on it
- Changed basket to have oak wood texture on bottom
- Changed weirwood faces to updated textures
- Fixed cobblestone hoppers not having biome shading
- Fixed eastern islands pebble layers not having biome shading
- Fixed some "arrow slit window" blocks being misnamed
- Fixed "arbor cobble stairs" being misnamed "oldtown cobble stairs"
- Fixed 3D ladder block model being undone
- Fixed marble column block models

# 12 July 2018:
- Added Dreadfort skeleton sconce blocks
- Added Glass Candle block for Citadel
- Added Oldtown sewer manhole
- Added Oldtown-specific brick & wattle blocks for Reach brick and Oldtown brick
- Added Valyrian Stone for Hightower base (minor Obsidian retexture + slabs/stairs)
- Added Starry Sept marble and Citadel green marble and variants
- Added Oldtown Brick and variants
- Added Arbor Brick and variants
- Added Reach panel wall blocks
- Added variants (stairs, slabs, etc.) of Lannisport Light Brick
- Added ornate arrow slits for small stone brick and light grey stone
- Added white grape leaves and crate
- Added more variants of weirwood faces
- Added mud slabs
- Added more basket variants
- Changed Reach stone to not be called "pink" anymore
- Changed Highgarden plasters to have more consistent / less neon colors
- Changed northern water and spruce leaf colors slightly
- Fixed "gray" vs. "grey" to be consistent ("grey" wins)
- Fixed some rogue pixels and inconsistent colors in great house banners
- Fixed mossy cobblestone not having new wall model

# 17 April 2018:
- Added block versions of wood half door texture
- Added light thatch and dark thatch wall blocks
- Added stair, slab, hopper, wall, fence variants for stormlands cobblestone
- Added basalt cobblestone and stair, slab, hopper, wall, fence variants
- Added sand layer blocks for wet sand, stony sand, and wet stony sand
- Changed normal ladder to have 3D block model
- Fixed dry and wet pebble textures being mixed rather than seperate

# 10 March 2018:
- Added pebble sand blocks & layers corresponding to each terrainset
- Added "done" and "WIP" utility blocks
- Added wall and fence oxidized bronze blocks
- Added brick-only version of "Small Bricks Ornate Top" block
- Added ash as custom block (to prevent it from disappearing under blocks)
- Added stone slab wall and fence blocks
- Added reach pink brick stairs, slabs, hopper, wall, fence
- Added stormlands brick stairs, slabs, hopper, wall, fence
- Added iron door variants of each wood type
- Added northern wood half door and hopper block
- Added wall blocks for each wood plank type
- Added window-type arrow slits for reach pink brick and stormlands brick
- Added ornate-type arrow slits for reach pink light stone and stormlands brick
- Added highgarden pink veined marble
- Added highgarden pink and green plasters
- Added vertical (ladder-style) falling water blocks
- Added banners for each of the great houses
- Changed tool blocks to be more realistic
- Changed oak shutter to be similar texture to oak door

# 14 August 2017:
- Changed carrot & radish crop textures to be better
- Changed torch texture to be a little more realistic
- Changed dirt to be mossy in Jungle biome
- Changed red sandstone to have horizontal CTM
- Changed new 1.11.2 vanilla textures to be DO NOT USE

# 13 March 2017:
- Added MDMeaux’s language pack (En_US) to change names of vanilla blocks
- Added brick hopper
- Added bronze wall and fence blocks
- Added lannisport slate wall and fence blocks
- Changed hues of Reach pink blocks & terrainset slightly to be more color-consistent
- Changed cattails to be slightly lighter in Extreme_Hills biome
- Changed “fancy” workbench blocks to have new texture
- Changed sides of wicker stool to wood
- Changed steel to darker iron on some textures
- Changed faint light grey stone in jungle_edge (riverlands) biome to be darker
- Changed bricks in jungle_edge (riverlands) biome to be slightly darker
- Changed Dorne Mud Plaster blocks to have sandstone border in Desert biome
- Changed cabinet blocks to remove book CTM tile
- Fixed sand in Birch_Forest (Westerlands) biome being wet rather than dry
- Fixed stone furnace being orange in desert biomes
- Removed grass tile from dirt ctm
- Removed patches on pale green & pale red beds

# 12 June 2016:
- Added “House Count” utility block
- Added White Harbor sewer manhole block
- Changed nettle hue to be less “plastic-y”
- Changed long grass to have more contrast, to make it more unique to the fern grass
- Changed glass to be less noisy and more transparent
- Changed cattails to be more random (to avoid tiling/grid issues)
- Changed dorne dry soil to be slightly darker to blend with dorne dirt
- Changed reach gravel to contrast less with dorne dirt, and to have a yellower hue
- Changed autumn leaf cover to be more nuanced/similar to the temperate leaf cover
- Changed new daub/wattle timber to be less fuzzy
- Changed mud & bog textures to be slightly toned down
- Changed sourleaf crop to have faint green stem & more muted colors
- Changed dirt/gravel to be slightly lighter
- Changed extreme_hills and jungle_edge biomes to be slightly lighter/less washed-out
- Changed halfdoor blocks to lighten dark part in center
- Changed hanging herb block to be less bright/saturated
- Changed snow to have a slightly whiter hue
- Changed muddy snow to have repeat CTM and less internal contrast
- Changed lady fern block to be darker and less blue
- Changed cow parsely block to have slight randomization
- Fixed shading issue with fruit/flower leaf blocks
- Fixed issue with mipmapping being forced off, even if enabled in settings
- Removed wheat animation

# 23 December 2015:
- Added “forest” biome for forests, with similar colors to plains but slightly greener
- Added “birch forest” biome for Westerlands, with more golden/mediterranean hues
- Added “jungle edge” biome for Riverlands, with darker, wetter colors
- Added six new forest undergrowth blocks for use in Forest biomes: Cow Parsely, Bracken, Lady Fern, Nettle/Dockleaf, Dead Bracken, and Fireweed.
- Changed most of the flower blocks to updated, better textures
- Changed potted plant flower types to more useful ones
- Changed some textures (grass/dirt/etc) to a different version in Forest biome
- Changed grass texture to a less “cartoon-ey” one
- Changed oak leaves to adapted Conquest texture
- Changed lightmaps to be more realistic and to apply in all worlds
- Changed fern and tall grass textures to be smoother (animated texture mixed in at random)
- Changed glass textures to appear more crystalline
- Changed cattail texture
- Changed wheat texture (now has subtle animation)
- Changed vine texture
- Changed obsidian (coal) to have slight random CTM
- Changed dirt and dorne dusty dirt textures
- Changed dorne dry soil texture
- Changed gravel and reach gravel textures
- Changed fallen leaf dirt texture to have two different types (autumn colors in the north, browner colors in temperate biomes)
- Changed fallen pine needle block to have repeat CTM
- Changed hay bale block to be darker
- Changed soul sand (tilled soil) to have new texture
- Changed farmland blocks to have new CTM
- Changed mossy log blocks to be slightly mossier
- Changed all daub/wattle textures so that the cross-hatches seem more like timber rather
than “sticks”

# 6 September 2015:
- Added new cattail block to replace buggy vanilla reed
- Added bowl of brown potshop profession sign (Flea Bottom only)
- Added WesterosBlocks brazier block to fix issues with vanilla one
- Added unlit brazier
- Added wood ladder block to replace vanilla ladder
- Changed firewood to not have iron clasps on the sides
- Fixed mossy small stone brick not having same color overlay as small stone brick in ocean biome

# 24 May 2015:
- Added “random” variant of iron throne sword texture
- Added dead rat texture specifically for Flea Bottom
- Added profession signs (only for use in large cities)
- Added dirty white version of Winterfell granite for White Harbor
- Added white cobble for White Harbor and corresponding stairs, slabs, and wall block
- Added stairs, slabs, wall, and arrow slit for Winterfell Granite
- Added Northern variants to hanging food (also added goose to fowl ctm in plains etc)
- Added hopper for small white brick
- Added black basalt transition block and corresponding stairs, slabs, wall, and arrow slit
- Added vertical CTM to arrow slits
- Added four blocks for Mance’s Camp: Two fur/hide blocks for common tents, one for Mance’s tent, and a canon antlers block for Mance’s tent only
- Added King’s Landing sewer manhole block
- Added new CTM for testing purposes to Winterfell Carving block
- Changed baelors orbs to new cuboid block model
- Changed lantern to emit less intense light
- Changed cobblestone and stone brick to be a little darker in Jungle biome
- Changed clay, red sandstone, and coarse red sandstone to be darker in Jungle biome
- Changed torch texture to have a more realistic sconce
- Fixed some blocks with incorrect top textures (northern carvings, mossy log)
- Fixed deprecated renderPass methods (3->overlay, 0->solid, etc)
- Fixed light gray wool being slightly different than the carpet
- Fixed oversaturated stained glass
- Fixed issues with transparent textures disappearing in front of glass blocks

# 3 February 2015:
- Changed grey stone textures and eastern island terrainset to be darker in Jungle biome
- Changed oak door texture to be more similar to our other door textures
- Fixed green grass instead of snow on the sides of grass blocks with snow on top
- Fixed biomes for minecraft version 1.7

# 25 December 2014:
- Added KL Dun brick and corresponding stairs, slabs, wall block, hopper, and arrow slit
- Added KL Dun brick cobble block and ornamental block
- Added candle altar block
- Added brown veined marble block for Baelors
- Added tentative marble types for Sunspear and Highgarden
- Added red sandstone ornamental and complementary brick with same texture as dun brick
- Added medium and small (wall and fence) sizes of sept crystal
- Added colored glass orbs with faint light output for Sept of Baelors
- Added stained glass blocks and panes for Baelors
- Added stairs, slabs, wall, and fence blocks of Iron Throne sword block
- Added red sandstone secret door for use in the Red Keep
- Added Alyssa’s Tears mist block- still might be changed and is NOT to be used anywhere other than its intended location
- Changed crystal block to have a newer, better texture
- Changed wheel block to be a little lighter
- Changed candle and red lantern blocks to be a little less bright
- Changed glowing coal block to be a bit brighter
- Changed hopper block to DO NOT USE
- Changed redstone torch and repeater/comparator blocks to DO NOT USE
- Changed piston top blocks to not have metal bits
- Changed bookshelves to not have metal borders
- Changed sept glass to have cast iron borders rather than steel ones (like normal glass)
- Changed hollow hopper block to be a little higher off the ground
- Changed paper block to have many CTM variants
- Changed iron throne sword block to match cast iron rather than steel
- Fixed glass flagon transparency
- Fixed thin line above wattle fence
- Fixed brazier sides disappearing from view and ash on bottom
- Fixed all stairs blocks labelled as “stair” to “stairs”
- Fixed CTM on vivid dark sandstone
- Fixed “Oak Half-Door” label to be “Oak Window Shutters”
- Fixed sand and sand layer blocks not having sand step sound
- Fixed Red Keep portrait still having metal borders

# 22 August 2014:
- Added hanging sourleaf
- Added “Sunflower” yellow flower variant
- Added slabs for basket blocks
- Added oak, birch, and jungle halfdoor blocks
- Added “hollow” hopper variants to replace vanilla one
- Changed hop texture to be darker
- Changed pie to have lattice top
- Changed lavender bush to be a little darker
- Changed Winterfell cobble ladder to be lighter
- Changed ocean & river terrainsets to look nicer
- Fixed small strip of Oldtown plasters

# 3 August 2014:
- Added updated night skybox
- Added flat wheel to replace shields used on wheelbarrows
- Added hop and olive leaves
- Added light grey stone, red sandstone, and small brick arrow slits
- Added red sandstone slabs and stairs
- Added dead frog block
- Added wattle fence
- Added rope and chain pulley blocks
- Added slab of Dorne Dusty Dirt
- Added mud furnace block for Dorne
- Added closed, open, apricot, date, lemon, lime, olive, orange, and pomegranate baskets
- Added hop, lavender, and sourleaf crates
- Added lavender bush
- Added sourleaf bush
- Added blood splatters with CTM
- Added palm tree leaves with distinct color overlay
- Added slabs & stairs of improved dark red brick texture
- Added subtle grass CTM
- Added 1x2 vertical painting, replacing the obsolete banner
- Added colormap to dead bush block to make it darker outside of Desert biome
- Added three new variants of 2006:10 flower type
- Added fallen weirwood leaves
- Added purple and brown plaster blocks and brick blocks specifically for Oldtown
- Added horizontal paper block to replace white carpet as paper
- Added oak and spruce panelling blocks for the Reach
- Added two star map blocks- one for 3x3, 3x1, and 1x3, and one for 2x2 and 1x1
- Added permanent Winterfell granite and cobble ladder blocks, winterfell carving finalized but texture not finished yet
- Added placeholder/WIP weirwood door and blue-veined marble blocks for Eyrie
- Added WIP sound blocks purely for serverside testing purposes- these are NOT, under any circumstance, to be placed by builders
- Changed stone, cobblestone, stone brick, and light grey stone to be darker in Ocean biome
- Changed icicle to be a bit darker
- Changed vanilla gold block to bronze-like texture
- Changed Night Watch bed & Northern bed to spruce wood base, pillow dirtier
- Changed blue noble bed base to non-golden
- Changed vanilla bad tuck to match patchy red bed
- Changed birch leaf texture
- Changed weirwood leaf texture
- Changed red sandstone to be dark in Desert biome
- Changed nether quartz (flagstone) to have less contrast
- Changed cake texture to pie
- Changed paintings to have wood corners
- Changed shield painting to have updated texture
- Changed clay to have new texture; made it red in Desert biome.
- Changed black, yellow, and pink banners to be one color
- Changed Dark Red Brick texture
- Changed barrel texture to have slightly rounded corners
- Changed strawberry plant texture
- Changed a couple flower blocks to have better-looking colors
- Changed potted plant selections to flowers which work better
- Changed red flowers 18 and 19 to red flower 22 type
- Changed crafting tables to have wooden tops
- Changed fish crate to be a fish barrel
- Fixed fruits not having an opaque fast graphics texture
- Fixed fruit leaf color discrepancies by changing fruits to “renderpass” method
- Fixed sand being wet in Savanna (Mushroom Island) biome
- Fixed running water texture not matching still water
- Fixed weirwood face block side textures being the wrong direction
- Fixed glass flagon and sept glass not being transparent
- Fixed CTM mistake on black and brown slate blocks
- Removed dispenser & dropper textures
- Removed vanilla hopper texture

# 25 June 2014:
- Added Reach terrainset
- Added ocean wave CTM
- Changed Iron Islands terrainset
- Changed Dorne/Arbor terrainset
- Changed Northern Mountains terrainset
- Changed Westerlands terrainset
- Changed Red Mountains terrainset
- Removed Winterfell Placeholder 4 (Wolf Gargoyle)

# 19 April 2014:
- Added sand layer blocks for red & orange sands
- Added stairs, slabs, fence, arrow slit, hopper, and wall block for pink Reach brick
- Added stairs, slabs, fence, hopper, and wall block for Stormland dark cobble
- Added 3 new sandstone types for Dorne
- Added stairs, slabs, fence, hopper, and wall block for each of the 3 new sandstones
- Added light grey stone and small stone brick hoppers
- Added Savanna grass/foliage colors on Mushroom Biome
- Added dark, wet colors to Jungle Biome for use in Stormlands
- Added thorny dead bush for Dorne
- Added two double(triple)-tall jungle grass blocks for the Rainwood
- Added double(triple)-tall savanna grass
- Added palm tree log, fence, and wall block
- Added brown and green slate variants
- Added new Stormlands terrain set
- Added Moorish wooden carving for Dorne
- Added Moorish stone carving for Dorne
- Added Sandstone carving & tile block with CTM for Dorne
- Added two mud plaster blocks for Dorne
- Added red shutter block for Dorne
- Added blue shutter block for the Reach
- Added yellow gravel path and slab for the Reach
- Added two transition soil types for Dorne transition
- Added ornate marble floor for Eyrie & Highgarden
- Added ornate sandstone floor for Dorne
- Added white flower varient of 2005:14
- Added cobble wall and fence blocks for each slate type
- Added wool slab blocks
- Added brick furnace block (normal and perma-burning)
- Added sandstone furnace block (normal and perma-burning)
- Added perma-burning stone furnace
- Added small red handheld lantern block
- Added full spruce door block
- Added sticky piston extension block
- Added horizontal (no-physics) rail block
- Added marble and brick fence types
- Added upside-down hopper block for gold brick texture
- Added slight random variations to cobblestone-like blocks
- Added oak hopper as WesterosBlocks block
- Added small white and grey stone bricks used on plaster CTM as actual blocks
- Added utility blocks to relay build information
- Added crimson plaster block, stairs, and slabs, for Casterly Rock ceilings
- Added mossy variant of light terracotta sandstone block
- Added placeholder blocks for Winterfell-specific textures
- Changed all slate types to be less noisy
- Changed wheat in North to be darker
- Changed flower probability on jasmine vines (increased)
- Changed arrow-slit blocks to allow arrows to be fired through them
- Changed the cobweb block to have CTM and better hue
- Changed log cobble wall variants to have “closed” tops
- Changed (rich) trapped chest to be less vibrant
- Changed bronze texture to be like oxidized bronze
- Changed gold brick block to be a little darker and less vibrant
- Changed dark blue Gulltown plaster to have grey stone brick keystones
- Changed Skybox to be less resource-intensive
- Changed birch color in all biomes to contrast less with other leaves
- Changed jungle color in some biomes to differ a little from oak
- Fixed Lannisport tile block in inventory
- Fixed pink wool color being off in two tiles
- Fixed sept glass not having new color sheen
- Fixed top and bottom textures of gearbox block
- Fixed (organized and optimized) internal structure of WesterosBlocks a bit
- Fixed fruit blocks having fruit on the top in some states
- Fixed foliage being white when using Optifine
- Fixed random dirt CTM on specific slate variants

# 1 December 2013:
- Added a glass Flagon
- Added a petwer Flagon
- Added a banner for the lord of light and the seven
- Added a generic banner for each wool colour
- Added Winch (Rope and Chain) for Spruce and Jungle Log
- Added Dragonstone block on Coal Block
- Added Bone block
- Fixed the Sword Rack
- Added a flat broken sword
- Added a wine flagon
- Made 4 Stormlands Blocks all of them are of a more earthen tone
- Added a Spit Roast Block
- Added Frying Pan
- Added a chili ristra
- Added a bushel of herbs
- Added a more orange brick texture for lannisport roofs
- Added Garlic strands
- Changed the hue of Sand in Plains, Forest, etc except for river, beach and ocean biomes.
- Changed the Desert Sandstone, Sand, Lapis and Stonebrick hue
- Added Northern Carvings block
- Added a lense flare effect to the sun
- Added a new moon
- Added a comet moving independently from the sun
- Added a skybox consisting of several moving clouds, fading in and out, during the day and a starry sky with a purple aurora at night.
- Added a Horse Shoe block
- Added Horizontal Net Block that behaves like the ropes for ships etc.
- Added 9 new bed blocks: Nights Watch, Northern Fur, Straw Berth, Itchy Straw Bed, Noble -Blue, Noble Red, Patchy Pale Red, Patchy Pale Green and a Hammock.
- Added Flour/ Grain Sack Slab
- Added Thatch Stair Blocks
- Added Stone Slab Stair Block
- Added Bushes and Vines
- Added vertical ctm for Mirror/Silver Block
- Replaced the melon with a squash block so that we can use them in game
- Reviewed the fruit blocks so they have better shading and the fruit are also behind the leaves.
- Changed non-canon potato to turnip crates.
- Adjusted the brightness on some of the Crate Goods and added a berry and a silver/tin ingot crate
- Changed Barrel Hoops and Nails on Crates to have a Wraught Iron look
- Added R'hllor image to dye_power_cyan 
- Fixed bottom of ender portal (jar)
- Fixed the fence vines and grapes
- Fixed glass block corners changed to wrought iron
- Fixed purple wool edges fixed on 176.png and 177.png
- Added Mossy Log Variants for Spruce, Birch and Jungle
- Changed netherrack to glowing coals that can be lit of fire.
- Made stairs that go well with the light stone slabs  
- Made Stone Slab Arrowslits
- Made Lannisport Plaster, Keystone Plaster in orange and yellow
- Made a shutter block for lannisport
- Made a brown light brick block for lannisport
- Made 4 Reach Blocks (3 of them pinkish one of them a paler variant of emerald)
- Made a regular Dorne Sandstone Arrowslit
- Made a Netherbrick Arrowslit
- Made a 24:4 Vivid Sandstone Arrowslit
- Made Fallen Leaves and Pine Needles Blocks
- Made a northern cobblestone and a frozen variant
- Made Tundra colder and fixed the leave colours
- Made Extreme_Hills into an autumn biome as transition for the North
- Made the Terrainset_North darker
- Made the Terrainset_Red_Mountains less random
- Made a GearBox-Block with animation on the RedstoneLamp
- Made a Swordrack
- Made a Hammock
- Fixed the Coins_on_the_ground_Block
- Changed Weirwood leaves
- Made a Cage Block
- Made a Brown Clay similar to the Mushroom Block
- Swapped around many of the textures; mainly: Emerald Block, Emerald Ore aswell as Lapis Block and Lapis Ore
- Fixed the names of the sandstone folder
- Fixed the names of the smoothstone folder


# 11 October 2013:
- Added hanging dead fish for smoking and drying
- Changed the lantern to a non-flickering less greenish orange
- Made the Colour of the Ocean Biome a darker grey.
- Changed the look of Iron Ore and Gold Ore
- Added Copper Ore, Silver Ore and Tin Ore.
- Made a clear and amber coloured lantern in both states (On/Off). We are currently using the amber version (On) as the glowstone texture. Let me know if you want a clear version that is toggleable for the redstone lamp.

# 7 October 2013:
- changed all the chains related textures to a darker Wrought Iron look
- made a new darker sign texture with a dark and light nail variation
- changed the sign.png item accordingly
- added a shieldboss
- added a wooden cabinet as the default cabinet
- moved the empty glass cabinet to sponge:4.
- changed the diamond_axe to look more like an arakh
- added a darker thatch block for the north as furs and dark thatch.
- added a leg of ham on 30:9 with the vertical ctm method.
- added 2 dead hares on 30:10 with the random method.
- added a dead chicken on 30:11 with the fixed method.
- added sausages on the single block of 30:9
- added a sliced variation of the leg od ham.
- added a rudimentary 8-way CTM for all of the Oldtown keystone/plaster combinations. (same method and style as the gulltown blocks)
- made the beacon block into a brazier (is a bit derpy but works)
- changed the glowstone:0 into a lantern with animation.

# 19 September 2013:
- Removed the Iron, Gold, and Diamond Horse Armor on request by Handsome_Dan
- Changed the Daylight Detector textures to ìDo Not Useî on request by Handsome_Dan
- Changed the Enchantment Table textures on request by Handsome_Dan
- Changed the Enchantment Table Book texture on request by Handsome_Dan
 

# 28 August 2013: 
- Fixed CTM on Blue and Ochre Whitewash
- Added Bog Texture by Moozipan

# 27 August 2013:
- Merged our Texture Pack Components with the Sound Pack Components. We will now coordinate our releases.
- Added Sn0wstorms ìnewî bog textures.
- Removed file debris that wasnít properly converted in the transition to resource packs.
- Added undead horse according to the specs pizza gave us.
- Added Blue Whitewash Stone Slab with CTM (159:9) and without (159:10) onto the coloured clay.
- Added Red Lanterns on 89:2 =>/ctm/lantern_red
- Added Vertical CTM for Reeds/Cattails and Random CTM for the single reed-block. 
- Added ìDO NOT USEî Texture for Monster Eggs ( Silverfish Spawn Blocks= 97)
- Fixed the bottom of the crate blocks
- Renamed all the Whitewash/plaster folders in /ctm to Plaster
- Made the large Smooth Stone and Double Slab Plaster Blocks to small bricks.
- Added experimental Cobblestone Plaster on (159:8)
- Added Double Slab Plaster with Dutchís pick in plaster material (159:9)
- Fixed farmland missing metadatas 2-15 for CTM.
- Moved 7 pointed star textures from whitewash CTM- Stone Brick to (159:5) and White Halfslabs to (159:6).
- Fixed missing ìmetadata=2î in Moozipanís mud texture, just updated the property files in his bin.
- Fixed stone brick cobble wall metadata being too dark- updated CTM folder and removed ì139:10î from stone brick colormap.
- Fixed glasspane sheen on net texture, put ìmetadata=0î on sheen property file.
- Fixed zombie texture- somehow changing the file from 128x64 to 128x128 fixed it.
- Made sand a little darker in ocean/beach/river biomes.
- Added Rivercobble ond 159
- Changed Oxidized Bronze Texture


# 15 April 2013:
- Added filled shelves to sponge block
- Added rich shelves to sponge block
- Added abandoned shelves to sponge block
- Added reddish daub & wattle
- Added brown daub & wattle
- Added brick daub & wattle
- Added animal skeleton in sand block
- Added ctm for patterned red brick
- Added open barrel with water
- Added winch log with chain N-S, E-W
- Added winch log with rope N-S, E-W
- Added cobblestone wall variations
- Added metadata for unidirectional weirwood faces
- Added 6-sided log textures as quartz metadata
- Added 6-sided piston texture as quartz metadata
- Added sandy stone brick
- Added all-top sandstone
- Added patterned wood, black chisled stone, and alt bedrock to stone brick
- Added upper slab for thatch and dirt
- Added double slab for thatch and dirt
- Added embedded axe, hatched, dagger, pickaxe, and shovel to fenceIron
- Added weirwood texture to quartz pillars
- Added 4 new workbench textures
- Added weirwood log fence texture
- Changed weirwood texture to birch wood texture
- Changed weirwood log fence texture to birch log fence texture
- Changed leaves and grapes on fences to be less contrasting and vibrant
- Changed brown stone texture to be darker and less purple
- Changed fire texture
- Changed scorched earth to include grass tufts, removed borders
- Changed rust on iron block, wrought iron block, fenceIron bars
- Changed smoke to be lighter
- Changed ocean biome to remove bluish hues
- Changed gray wood to be less silver by removing highlights
- Changed reinforced iron fence texture to include larger studs
- Changed tnt texture to read "Do not use!"
- Removed emerald crystal texture

# 11 April 2013:
- Added rusted iron, wrought iron, rusted wrought iron, bronze, and oxidized bronze blocks to blockIron metadata
- Added gold brick block to blockGold metadata
- Added pink, light brown, and dark brown wool to plank metadata
- Added purple wood to plank metadata
- Added light blue and green crystals to ice metadata
- Added glowing coals to glowstone metadata
- Added glowing coals to netherrack metadata
- Added cobble and mossy cobble fences to netherbrickfence metadata
- Added reinforced wooden fence to netherbrickfence metadata
- Added charred log to netherrack metadata
- Added biome dependence to sandstone slabs and stairs for alternate color
- Added biome dependence for lapis ore in ocean biomes
- Added thatch and dirt slabs
- Added brown stone to sandstone metadata
- Added biome dependence for darker sandstone stairs and slabs
- Added alt red sandstone to biomes
- Rearragned sandstone textures
- Rearranged biome dependencies for ladders, quartz stairs/slabs, sandstone stairs/slabs, and red sandstone
- Moved flagstone variants to sandstone metadata for middle-click selection
- Changed chest texture to wrought iron corners
- Changed yellow and white woods to be less vibrant
- Changed water color in Plains and ExtremeHills biomes
- Changed goldNugget to uncolored gold dragon
- Changed shovelGold to brown crossbow
- Changed color of lilies on lilypads
- Changed blockRedstone to barrel
- Changed redstone ore back to original texture
- Changed marble pillar texture to "Do Not Use"
- Changed mushroom_top pillars to quartz pillar top square design
- Fixed alchemist bookshelf corner glitch
- Fixed sandstone slab stair and slab tops
- Fixed birch/jungle stair glitch
- Fixed colored wood top colors
- Fixed colored wood nail colors
- Fixed nails on planks, fence, bookshelf, and sponge tops to be darker and less yellow
- Fixed ctm on all wood plank tops

# 29 March 2013:
- Changed color of rain splashes to light blue
- Changed color of lava drip to blood
- Removed lily pad color in color.properties
- Changed color of cats and 
- Changed char.png to metal helmet
- Changed creeper.png to lighter cabbage head
- Added better skies
- Added better comet
- Added better clouds
- Added heavier, blue-colored rain
- Added heavier, gentler snow
- Changed swampland biome to be more vibrant (changed hue and saturation)
- Changed cattail texture to match biomes
- Replaced torch texture
- Changed dyes to images of The Seven
- Changed inventory images for multiple items
- Retextured red mushroom top to chalk stone
- Retextured nether quartz ore to flagstone and 3 other ctm varieties
- Retextured redstone repeater and comparitor with pool of wax
- Retextured ender portals and eyes as clay pots with corks
- Retextuerd hopper as wooden support
- Retextured free slab as cushions
- Retextured carrots and turnips (potatoes) to have less brightness
- Retextured trap chest
- Retextured enchantment table
- Retextured rails to better carpets
- Retextured jackolantern as haybale with target
- Added ctm for bookshelves: regular, empty, alchemist, and library
- Added handle to cauldron texture
- Added ctm to cobwebs: vertical rope, chain, and smoke
- Added ctm to crafting tables: kitchen, workshop, dining table
- Retextured diamond ore to alt sandstone
- Added ctm to dirt to include rocks, bones
- Added ctm to endstone for daub & wattle designs
- Added ctm to fences for all plank, vine, and grapevine combinations
- Added ctm to fences for log textures
- Retextured fenceIron bars to be wrought iron
- Added ctm to fenceIron to remove top/bottom bar, add rusted bars, clothesline
- Added flowers to metadatas for random and individual selection
- Added ctm to glass blocks and panes
- Added ctm to grasses for biome-tailored and biome-dependent selection
- Added ctm for cracked ice in biomes
- Added ctm for colored crystal
- Added ctm for ladders, rope ladders, and iron rungs
- Added biome dependence for lilypads
- Added mushrooms to metadatas for random and individual selection
- Added ctm to mushroom_top for pillars
- Retextured mycelium as ash
- Added ctm to mycelium for scorched earth
- Added ctm to noteblocks for open crates with comodities
- Retextured kz.png to include medieval paintings and tapestries
- Retextured kz.png to replace wrought iron shackles and shield
- Retextured weighted pressure plates as full and empty trencher/plate
- Added biome dependence to quartz stairs and slabs for gray slate, red tile, and blue tile
- Added biome dependence to sandstone stairs and slabs for new light sandstone
- Added ctm to sand to include shells and rocks in river/ocean biomes
- Added 10 types of sandstone texture
- Added biome dependence for saplings
- Added ctm to snow to randomize
- Added ctm to sponge to connect cupboards
- Added wood tops to sponge
- Added ctm to tnt to give 2 or 3 iron rings
- Added cmt to wood planks to connect on the tops, with borders on the sides
- Added black, grey, white, and yellow planks
- Added ctm to wool with borders on corners


# 5 January 2013:
- Changed potatoes to turnips
- Changed nether wart to pease
- Changed sugar cane to cattails
- Changed creeper heads to cabbage
- Changed wither head skulls to human skulls
- Retextured lapis ore to dark gray accent brick
- Retextured block emerald to light gray accent brick
- Increased contrast in cobblestone for more definition
- Added red tops to smooth sandstone
- Updated diamond block (mirrors) to have less wood border
- Updated backgrounds behind ores to match stone
- Changed inventory slots for potatoes (turnips) and nether wart (pea pods)
- Changed inventory slots for music discs to house sigils for item frame use

# 21 August 2012:
- Updated Texturepack to 1.4 with Textures from Dokucraft

# 21 August 2012:
- Changed the Jungle wood Texture to a Dark Grey Log.

# 18 August 2012:
- Improved the resolution of the Red Comet. (Better Skies)
- Changed the Star Skybox to one that matches our texture resolution better. (Better Skies)
- Reduced the size of the Sun.
- Changed Emerald Ore to the Werewood faces.
- Updated the Cocoa bean texture to those of the Dokucraft TP. (Cocoa Plant By Verruckt)
- Updated the Tripwire texture to those of the Dokucraft TP. (Tripwire By Noodaa)

# 17 August 2012:
- Incorporated Better Skies to allow for a stationary red comet. (Using some of Misa's Realistic Texturepack assets temporarily)
- Reduced the drip rate of the redstone torch (candles).

# 16 August 2012:
- Changed the TNT block to a Sealed Barrel.
- Fixed the CTM so it matches our sandstone hue.

# 8 August 2012:
- Darkened the lighter bricks in the brick texture.
- Fixed some textures that did not carry over in the 1.3.1 TP update.

# 7 August 2012:
- Updated Pack for 1.3.1 Compatabilty
- Retextured Wood Doors to appear more rustic
- Retextured Nether Warts to look like Carrots (Marken)
- Retextured Emerald Blocks into a rainbow texture to make your eyes bleed; so don't use it. (Marken)

# 4 August 2012:
- Retextured Minecart Rails (Corners) to be rope. (Marken)
- Retextured Endstone into a Reinforced Daub and Waddle. (Marken)

# 26 July 2012:
- Logs now have darker wood grain. (Marken)
- Retextured Steel doors as wood to provide more uses. (Marken)
- Retextured Iron Bars to remove their horizontal bars. (Marken)
- Retextured Minecart Rails to be rope. (Marken)
- Retextured Endstone into a rainbow texture to make your eyes bleed; so don't use it. (Marken)
- Retextured Wool to a desaturated color scheme. (Marken)

# 21 July 2012:
- Removed Red Stone Bricks(Endstone).

# 18 July 2012:
- New and Improved WesterosCraft Logo for main menu.(Hevnlyst)
- New Game of Blocks Logo replaces Mojang Splash. (Hevnlyst)
- Retextured Pink wool to act as green planks. (Marken)
- New Gravel Texture for less eye bleeding. (Marken)

# 2 July 2012:
- Changed the Red Mushroom back, will be adding the woven looking block when custom blocks happen.
- Changed Bed item to a slightly darker brown to match the bed blocks(Arezeus)
- Fixed White Wool not being seamless (Seamless By DeathStar1710)

# 28 June 2012:
- Changed Wool to a Seamless variant (Seamless By DeathStar1710)
- Change Endstone to a nice red brick (SMP)
- Changed Piston Foot to have a wooden to look for better tables (Arezeus)
- Changed Beds to a slightly darker brown (Arezeus)
- Changed Enderportal frame to look like a jug and eye of ender is now a plug for it (Arezeus)
- Changed Texturepack Image to our new wax seal Logo (Arezeus)
- Re-Textured Red Mushroom block to a woven basket looking thing (Arezeus)
- Added black outline on Main Menu logo text (Arezeus)
- Removed crosses on the sides of menu buttons (Arezeus)

# 20 June 2012:
- Added New Artwork by Mgleim

# 19 June 2012:
Intergrated changes made by Mgleim, SMP, and Teqna
- New title and high-res font provided by Teqna
- New Glowstone, to look better as a standalone Lantern block
- Changed Redstone Lamp Texture, removed the vertical bars from the block
- Changed Glass Texture, to look more like medieval windows
- Changed Cobweb Texture to a darker variant
- Changed Crafting Table to a stone topped variant
- Changed Decorative Sandstone to look like Sandstone
- Changed the Blackpowder crate (TNT) texture to a more decorative one without the XXX on the side
- Changed to a more neutrally-colored bed (Brown)
- Added SMP's improved chests with no glitching rims
- Smoke Particles have been changed so they no longer look cartoony
- Very minor tone changes in certain blocks (SMP - cobble and stone bricks, Mgliem - pistons, furnaces, clay, and bedrock)

# 16 June 2012:
- Changed Trapdoor to be Windowless, for greater flexibility in detailing
- Changed Bedrock to look like dark cobblestone
- Changed Ice blocks to a more white color
- Changed Obsidian to have a darker color
- Changed Beds to a Green Color
- Changed Powered Rail(off) to a Red Carpet
- Changed Powered-Rail(on) to a Blue Carpet
- Changed the Grass and vegetation textures so they no longer appear purple in swamps.

# 12 June 2012:
- Re-Textured the Jungle Log to look like a bleeding werewood face (werewood face by Dutchguard)
- Re-Textured the diamond axe to look like an Arakh (Dothraki Arakh by Nanocon1)
- Re-Textured the gold axe to look like an Arakh (Dothraki Arakh by Nanocon1)
- Re-Textured the gold sword to look like a Dagger (Dagger by Nanocon1)
- Removed the Grass and vegetation texture changes because of unintended color shifts.

# 11 June 2012:
- Re-Textured the wood sword to look like an Arakh (Dothraki Arakh by Nanocon1)
- Changed the Grass and vegetation textures so they no longer appear purple in swamps.

# 03 June 2012:
- Changed Yellow wool from its Lannister gold variant to a more universally useful one.

# 01 June 2012:
- Changed "Use the Patcher Noob" to "Use MCPatcher to Fix, Google It."

# 24 May 2012:
- Created a WesterosCraft to replace the Minecraft Logo.
- Created a Texture Pack Icon (Game of Blocks)
- Created a byline for the Texture Pack
- Fixed Valyrian(diamond) sword texture
- Changed Font to a Fancy Gradient
- Changed the Mob Cage Block to look more like Vanilla (Dokucraft Default-esque by SerAaron)
- Changed Sugar cane look more like reeds (Dokucraft Sugarcane by zaph34r)
- Changed Diamond Blocks/Ore/Item to a clear(white) variant (Dokucraft White Diamond alt by Kiajinn)
- Changed Some Paintings to some more appropriate ones (Landscape,Light, HillRuins by Smuecke,UNDEADCHICKENNUGGET,TWiiSt3D)
- Changed the Iron Doors to remove the Templar cross(Dokucraft Crossless - By dubca7)
- Changed the Repeaters (Dokucraft Repeater By Oys)
- Changed the Book (Dokucraft Book Brown - By SerAaron)
- Re-textured Bowls to appear as Mugs
- Re-textured Mushroom Stew to appear as Beer
- Re-textured TNT to look like Black Powder
- Re-Textured Clay item to red like the block itself
- Re-textured Redstone Torches to be Candles (New Candle and Animation by Marken)
- Re-textured Leather Armor to appear as Nights Watch/Bandit Armor
