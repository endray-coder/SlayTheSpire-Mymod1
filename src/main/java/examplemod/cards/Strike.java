package examplemod.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Strike extends CustomCard {
    public static final String ID = "mymod1:Strike";
    private static final String NAME = "超绝故障机器人杀鸡乱斩";
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    // 1. 这张卡的累计成长伤害（每张卡独立拥有，初始为0）
    private int cardExclusiveGrowth = 0;
    // 2. 每次使用后，这张卡的永久成长值（可自定义，这里设为1）
    private final int PER_USE_GROWTH = 1;
    public Strike() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = this.damage =1;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() { // 升级调用的方法
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeDamage(3); // 将该卡牌的伤害提高3点。
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
        int atktime = 3;
        // 备份当前基础伤害（用于循环结束后，只保留1点战斗内成长）
        int backupBaseDamage = this.baseDamage;

        for (int i = 0; i < atktime; i++) {
            // 步骤1：让框架基于当前 baseDamage 计算最终伤害（含愤怒倍率）
            this.applyPowers();
            // 步骤2：结算伤害（用框架计算好的 damage，这是带倍率的最终值）
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(
                            m,
                            new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)
                    )
            );
            // 步骤3：递增「基础伤害」1点（这是关键，让下一次循环有新的底牌）
            this.baseDamage += 1;
            // 步骤4：同步 damage，让框架下一次计算倍率时识别新的 baseDamage
            this.damage = this.baseDamage;
        }

        // 步骤5：循环结束后，只保留1点战斗内成长（符合你的需求）
        this.baseDamage = backupBaseDamage + 1;
        this.damage = this.baseDamage;
    }



}
