package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.burn;
import examplemod.powers.bullet;

public class shoot1 extends CustomCard {
    public static final String ID = "Leiheng:shoot1";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public shoot1() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 4;
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
            this.rawDescription = UPGRADE_DESCRIPTION;
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
        }

        // 检查是否拥有足够的子弹，有则消耗并触发额外效果
        int consumed = bullet.consumeBullets(p, 1); // 消耗1层子弹
        if (consumed > 0) {
            // 触发额外效果：造成额外伤害
            int extraDamage = this.upgraded ? 3 : 2;
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m,
                    new DamageInfo(p, extraDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.FIRE
            ));
            
            // 施加烧伤层数（使用magicNumber变量）
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        m,
                        p,
                        new burn(m, this.magicNumber),
                        this.magicNumber
                ));

        }
    }
}