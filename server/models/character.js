import { Alignment } from "./alignment";
import { Background } from "./background";

export class Character {
    characterId = '';
    characterName = '';
    experiencePoints = 0;
    background = new Background();
    currentHitPoints = 0;
    maxHitPoints = 0;
    alignment = new Alignment();
    personalityTraits = '';
    ideals = '';
    bonds = '';
    flaws = '';
    feats = new Array();
    languages = new Array();
    toolProficiencies = new Array();
    skillProficiencies = new Array();

    constructor() {}
}