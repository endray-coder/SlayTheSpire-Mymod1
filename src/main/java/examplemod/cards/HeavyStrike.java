package examplemod.cards;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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
public class HeavyStrike extends CustomCard {
    public static final String ID = "mymod:HeavyStrike";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HeavyStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage=15;
        this.baseMagicNumber=8;
        this.magicNumber=this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(7);
            this.upgradeMagicNumber(5);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
        // 检查是否拥有足够的子弹，有则消耗并触发额外效果
        int bulletsToConsume = 2;
        if (bullet.consumeBullets(p, bulletsToConsume)) { // 消耗2层子弹
            // 施加烧伤层数（使用magicNumber变量）
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    m,
                    p,
                    new burn(m, this.magicNumber),
                    this.magicNumber
            ));
            
            // 触发额外效果：造成额外伤害
            for(int i=0;i<bulletsToConsume;i++) {
                int extraDamage = this.upgraded ? 4 : 2;
                int extraBurn=this.upgraded ? 4 : 2;
                AbstractDungeon.actionManager.addToBottom(new DamageAction(
                        m,
                        new DamageInfo(p, extraDamage, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.FIRE
                ));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        m,
                        p,
                        new burn(m, extraBurn),
                        extraBurn
                ));
            }
        }
    }
}