// 创建一个名为TestBulletCard的新卡牌
package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.bullet;

public class Reloading1 extends CustomCard {
    public static final String ID = "mymod:Reloading1";
    private static final String NAME = "测试子弹能力";
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = "获得 !M! 层 #y虎标弹";
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Reloading1() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2); // 升级后增加2层能力
            this.rawDescription = "获得 !M! 层 #y虎标弹";
            this.initializeDescription();
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用bullet能力
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, // 目标
                p, // 来源
                new bullet(p, this.magicNumber), // 你的自定义能力
                this.magicNumber // 层数
        ));
    }
}