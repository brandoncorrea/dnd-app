(ns dnd.creaturec)

(def all
  [
   {:id           1
    :name         "Bat"
    :size         "tiny"
    :kind         "beast"
    :ac           12
    :hp           "1 (1d4 – 1)"
    :ground-speed 5
    :air-speed    30
    :strength     2
    :dexterity    15
    :constitution 8
    :intelligence 2
    :wisdom       12
    :charisma     4
    :challenge    0
    :senses       ["blindsight 60ft." "passive Perception 11"]
    :traits       [{:name        "Echolocation"
                    :description "While it can't hear, the bat has no blindsight."}
                   {:name        "Keen Hearing"
                    :description "The bat has advantage on Wisdom (Perception) checks that rely on hearing."}]
    :actions      [{:name        "Bite"
                    :description "Melee Weapon Attack: +0 to hit, reach 5 ft., one creature. Hit: 1 piercing damage."}]
    }
   {:id           2
    :name         "Black Bear"
    :size         "medium"
    :kind         "beast"
    :ac           11
    :hp           "19 (3d8 + 6)"
    :ground-speed 40
    :climb-speed  30
    :strength     15
    :dexterity    10
    :constitution 14
    :intelligence 2
    :wisdom       12
    :charisma     7
    :challenge    0.5
    :skills       ["Perception +3"]
    :senses       ["passive Perception 13"]
    :traits       [{:name        "Keen Smell"
                    :description "The bear has advantage on Wisdom (Perception) checks that rely on smell."}]
    :actions      [{:name        "Multiattack"
                    :description "The bear makes two attacks: one with its bite and one with its claws."}
                   {:name        "Bite"
                    :description "Melee Weapon Attack: +4 to hit, reach 5 ft, one target. Hit: 5 (1d6 + 2) piercing damage."}
                   {:name        "Claws"
                    :description "Melee Weapon Attack: +4 to hit, reach 5 ft, one target. Hit: 7 (2d4 + 2) slashing damage."}]}
   {:id           3
    :name         "Boar"
    :size         "medium"
    :kind         "beast"
    :ac           11
    :hp           "11 (2d8 + 2)"
    :ground-speed 40
    :strength     13
    :dexterity    11
    :constitution 12
    :intelligence 2
    :wisdom       9
    :charisma     5
    :challenge    0.25
    :senses       ["passive Perception 9"]
    :traits       [{:name        "Charge"
                    :description "If the boar moves at least 20 feet straight toward a creature right before hitting it with a tusk attack, the target takes an extra 3 (1d6) slashing damage and must succeed on a DC 11 Strength saving throw or be knocked prone."}
                   {:name        "Relentless (Recharges after the Boar finishes a Short or Long Rest)"
                    :description "If the boar takes 7 damage or less that would reduce it to 0 hit points, it is reduced to 1 hit point instead."}]
    :actions      [{:name        "Tusk"
                    :description "Melee Weapon Attack: +3 to hit, reach 5 ft., one target. Hit: 4 (1d6 + 1) slashing damage"}]}
   {:id           4
    :name         "Brown Bear"
    :size         "large"
    :kind         "beast"
    :ac           11
    :hp           "34 (4d10 + 12)"
    :ground-speed 40
    :climb-speed  30
    :strength     19
    :dexterity    10
    :constitution 16
    :intelligence 2
    :wisdom       13
    :charisma     7
    :challenge    1
    :skills       ["Perception +3"]
    :senses       ["passive Perception 13"]
    :traits       [{:name        "Keen Smell"
                    :description "The bear has advantage on Wisdom (Perception) checks that rely on smell."}]
    :actions      [{:name        "Multiattack"
                    :description "The bear makes two attacks: one with its bite and one with its claws."}
                   {:name        "Bite"
                    :description "Melee Weapon Attack: +6 to hit, reach 5 ft, one target. Hit: 8 (1d8 + 4) piercing damage."}
                   {:name        "Claws"
                    :description "Melee Weapon Attack: +6 to hit, reach 5 ft, one target. Hit: 11 (2d6 + 4) slashing damage."}]}
   ]
  )
