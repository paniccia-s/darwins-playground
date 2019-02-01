# darwins-playground
A personal project in Java! 

Below is a relatively exhaustive description of the gameplay, which I've copy-pasted from a drafting document into here. Anything in square brackets is questionable. 

Tl;dr: breed, mutate, and simulate organisms to permit their survival. 

#### Project begun in October of 2017. 


# Darwin's Playground: Mutate your way to victory! 

Premise: An evolutionary simulation in which organisms are mutated, bred, and placed into a world where their survival is based off of how well they were genetically engineered. 

Gameplay: Gameplay is turn-based, where the player may execute one of several actions per turn (or no action at all). Gameplay continues until a turn limit is reached. Gameplay is divided into scenarios of increasing difficulty. The default win condition of every scenario is the same: evolve your organism(s) to permit survival in the world before the turn limit is up.

Win Condition: Once the turn limit has been reached, all organisms currently living in the world are simulated over some number of additional game ticks. During this simulation, organisms live or die as they would in any other turn. If at least one organism survives the simulation period, the player wins. The player may not act in any capacity during these turns. 

# Sample Gameplay Screenshots

The game starts with your choosing to load a saved game or to create a new one:


Game Concepts: 
1.	The Scenario: The current level. Every scenario contains different organisms, a different turn limit, different genes [and a different world?]. 
2.	The Organism: Every scenario provides one or more different types of organisms to the player. Each is given a [standard or random?] genome. To win the scenario, the player must engineer their organisms to survive in the simulation period.
3.	The World: The world is a 2-D representation of a planet far, far away. The world contains a [standard or random?] combination of biomes. The world is displayed as a grid of different colors, where each color represents a unique biome. 
4.	The Biome: A biome is a specific region of the world. Each biome has specific characteristics that can influence an organism’s ability to survive in that biome. Examples of characteristics include temperature, humidity, nutrient concentration, etc. [Is information about the biome known, or must research be done to learn?]
The Turn System: In a turn, the player may perform one of the following actions: 
1.	Mutate: Mutates the selected organism. This action presents a chance for mutation to occur within the organism. If successful, mutation will present a new version of a gene (an allele) within the organism. The gene that mutates is random, and the allele that it provides is also random. Genes may have any number of alleles. Alleles may be beneficial, harmful, or insignificant, and a mutated allele’s function is not known upon discovery. If the mutation action fails, nothing happens and the turn ends. 
2.	Breed: Breed two organisms to create a third. Breeding functionality is Mendelian: each organism has two copies of a gene, and the offspring will have some combination of both parents’ genomes. The passing down of genes is random; there is no way to ensure that any specific allele gets passed down to offspring. Breeding may only occur between organisms of opposite gender [and organisms cannot reproduce with any of their ancestors].
3.	Place: Place the selected organism into the world. This action puts the organism in its present state into the world at a chosen location. Once an organism is in the world, it may not be mutated or bred. See “Living in the World” for what happens after an organism is placed. 
4.	Autopsy: Autopsies a deceased organism. Autopsying presents a chance to discover information about why and how it died. If successful, autopsy will provide any amount of information concerning the organism’s death. Information possible to obtain includes biome impact on death, genetic impact on death, and the functionality of active alleles. If autopsying fails, nothing happens and the turn ends.
5.	Observe: Observes a living organism. Observing an organism presents all relevant information about the organism, including species, location (in/out of world), and valid allele selections. Here, the player may change that organism’s genome, changing alleles of any gene to any other discovered alleles of that gene. Observing does NOT take a turn, regardless of what is actually done in the Observe stage. 
6.	[Research: Research a selected biome. Researching presents a chance to discover information about the biome. If successful, researching will provide any amount of information concerning this biome. If researching fails, nothing happens and the turn ends.] [Is this good?]

Living in the World: Every turn, all placed organisms are simulated within the world. Organisms live in a hostile world: their genome is the only thing between them and death [for now]. For each simulated turn, every placed organism has some chance of dying; this chance is calculated based on the biome in which the organism was placed and the benefits its genome provides. More fit organisms will be better suited to survive. Some genomes promote survival in certain biomes over others. 
Death: if an organism fails to survive in the world at the end of a turn, it will die at the start of the next turn. A deceased organism may not be mutated or bred. A deceased organism may be autopsied [up to three times or infinitely?] to gather information about its death. Additionally, no organism may be placed on a world tile that contains a dead organism. [Once a deceased organism is autopsied three times, its corpse disappears from the world and it may not be interacted with further.]
