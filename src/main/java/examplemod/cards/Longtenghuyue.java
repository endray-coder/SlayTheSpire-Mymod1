package examplemod.cards;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.bullet;
import examplemod.powers.burn;

// 在examplemod.cards包下创建Defend.java
public class Longtenghuyue extends CustomCard {
    public static final String ID = "mymod:Longtenghuyue";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Longtenghuyue() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber=2;
        this.magicNumber=this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        bullet bulletPower = (bullet) p.getPower(bullet.POWER_ID);

        int times = 0;
        if (bulletPower != null && bulletPower.amount >= 1) { // 消耗1层子弹
            int bulletsToConsume = 1;
            times = Math.min(bulletPower.amount, bulletsToConsume);
            if (bulletPower.amount > bulletsToConsume) {
                // 如果子弹数量多于需要消耗的，减少子弹层数
                bulletPower.amount -= bulletsToConsume;
                bulletPower.updateDescription();
            } else {
                // 如果子弹数量正好或少于需要消耗的，移除子弹能力
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, bulletPower));
            }

        }

        for (int i = 0; i < 1 + times; i++) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        }

    }
}