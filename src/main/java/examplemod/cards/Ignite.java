package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.burn;

public class Ignite extends CustomCard {
    public static final String ID = "Leiheng:Ignite";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Ignite() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3); // 升级后基础伤害+2
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算伤害：基础伤害 + 目标燃烧层数
        int burnCount = 0;
        burn burnPower = (burn) m.getPower(burn.POWER_ID);
        if (burnPower != null) {
            burnCount = burnPower.amount;
        }
        int totalDamage = this.damage + burnCount/2;
        
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m,
                new DamageInfo(p, totalDamage, this.damageTypeForTurn)
        ));
    }
}