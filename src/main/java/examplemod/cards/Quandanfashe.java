package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.bullet;

/**
 * 全弹发射：把攒下来的虎标弹一次性全部打出去换成伤害。
 * 走 bullet.consumeBullets，所以「换膛续击 / 弹幕防御」也会照常触发。
 */
public class Quandanfashe extends CustomCard {
    public static final String ID = "leiheng:Quandanfashe";
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

    public Quandanfashe() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 5;
        this.damage = this.baseDamage;
        this.baseMagicNumber = 2; // 每层虎标弹的额外伤害
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.upgradeMagicNumber(1);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        bullet bulletPower = (bullet) p.getPower(bullet.POWER_ID);
        int count = (bulletPower != null && bulletPower.amount > 0) ? bulletPower.amount : 0;
        int dealt = this.damage + count * this.magicNumber;

        // 先结算消耗（会顺带触发换膛续击/弹幕防御），再打伤害
        if (count > 0) {
            bullet.consumeBullets(p, count);
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m,
                new DamageInfo(p, dealt, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.FIRE
        ));
    }
}
