package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import examplemod.character.MyCharacter;

public class QuickStrike extends CustomCard {
    public static final String ID = "mymod1:QuickStrike";
    private static final String NAME = "快机乱麻";
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = "造成 !D! 点伤害3次。每次 !M!% 概率增加6点";
    private static final CardType TYPE = CardType.ATTACK;
    //private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardColor COLOR =CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    public static final double rate=0.7;

    private int atktime =3;
    public QuickStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = this.damage =3;
        // 使用baseMagicNumber存储概率值（百分比形式）
        this.baseMagicNumber = this.magicNumber = (int)(rate * 100);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() { // 升级调用的方法
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
           // this.upgradeDamage(3); // 将该卡牌的伤害提高3点。
            this.atktime =5;
            this.name="超绝猛机杀击乱斩";
            this.rawDescription = "造成 !D! 点伤害5次。每次 !M!% 概率增加6点";
        }
    }

    /**
     * 当卡牌被使用时，调用这个方法。
     *
     * @param p 你的玩家实体类。
     * @param m 指向的怪物类。（无指向时为null，包括攻击所有敌人时）
     */

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 备份当前基础伤害（用于循环结束后，只保留1点战斗内成长）
        int backupBaseDamage = this.baseDamage;

        for (int i = 0; i < atktime; i++) {
            // 步骤1：让框架基于当前 baseDamage 计算最终伤害（含愤怒倍率）
            if (Math.random() < rate) {
                this.baseDamage += 6;
                // 添加伤害提升的视觉效果
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.LIGHTNING));
            }
            this.damage = this.baseDamage;
            this.applyPowers();
            // 步骤2：结算伤害（用框架计算好的 damage，这是带倍率的最终值）
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(
                            m,
                            new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)
                    )
            );


        }

        // 更新魔法值（概率）
        this.baseMagicNumber = (int)(rate * 100);
        this.magicNumber = this.baseMagicNumber;
        //当时测试用，本来这张卡是造成伤害成等差数列上升用的，后面想了想干脆搞成超绝猛虎杀鸡乱斩得了
       // this.baseDamage = backupBaseDamage + 1;
        this.baseDamage = backupBaseDamage ;
        this.damage = this.baseDamage;
    }



}