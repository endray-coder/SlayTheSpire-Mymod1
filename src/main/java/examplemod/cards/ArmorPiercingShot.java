package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;

public class ArmorPiercingShot extends CustomCard {
    public static final String ID = "Leiheng:ArmorPiercingShot";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ArmorPiercingShot() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 12;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.upgradeMagicNumber(1);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)
        ));
        
        // 检查是否拥有子弹，有则消耗并给予debuff
        int bulletsToConsume = 1;
        int consumed = examplemod.powers.bullet.consumeBullets(p, bulletsToConsume);
        if (consumed > 0) {
            // 给予敌人虚弱
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    m,
                    p,
                    new com.megacrit.cardcrawl.powers.WeakPower(m, this.magicNumber, false),
                    this.magicNumber
            ));
            // 给予敌人易伤
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    m,
                    p,
                    new com.megacrit.cardcrawl.powers.VulnerablePower(m, this.magicNumber, false),
                    this.magicNumber
            ));
        }
    }
}