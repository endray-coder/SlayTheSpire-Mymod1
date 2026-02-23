package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.burn;
import examplemod.powers.bullet;

public class shoot1 extends CustomCard {
    public static final String ID = "mymod:shoot1";
    private static final String NAME = "双重射击";
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = "攻击敌人2次，每次造成 !D! 点伤害并施加 !M! 层 mymod:燃烧 。消耗 1 层 mymod:虎标弹 ，造成额外 1 点伤害。";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public shoot1() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 6;
        this.damage = this.baseDamage;
        this.baseMagicNumber = 3; // 设置基础烧伤层数
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2); // 升级后每次伤害+2
            this.upgradeMagicNumber(2); // 升级后每次烧伤层数+1
            this.rawDescription = "攻击敌人2次，每次造成 !D! 点伤害并施加 !M! 层 mymod:燃烧 。消耗 1 层 mymod:虎标弹 ，造成额外 2 点伤害。";
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 执行两次攻击（基础效果，无论是否有子弹都执行）
        for (int i = 0; i < 2; i++) {
            // 造成伤害
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));

            // 施加烧伤层数（使用magicNumber变量）
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    m,
                    p,
                    new burn(m, this.magicNumber),
                    this.magicNumber
            ));
        }

        // 检查是否拥有足够的子弹，有则消耗并触发额外效果
        bullet bulletPower = (bullet) p.getPower(bullet.POWER_ID);
        if (bulletPower != null && bulletPower.amount >= 1) { // 消耗1层子弹
            // 消耗子弹
            int bulletsToConsume = 1;
            if (bulletPower.amount > bulletsToConsume) {
                // 如果子弹数量多于需要消耗的，减少子弹层数
                bulletPower.amount -= bulletsToConsume;
                bulletPower.updateDescription();
            } else {
                // 如果子弹数量正好或少于需要消耗的，移除子弹能力
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, bulletPower));
            }

            // 触发额外效果：造成额外伤害
            int extraDamage = this.upgraded ? 2 : 1; // 升级后额外伤害为2，基础为1
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m,
                    new DamageInfo(p, extraDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.FIRE
            ));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    m,
                    p,
                    new burn(m, 2),
                    2
            ));
        }
    }
}