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
import com.megacrit.cardcrawl.powers.WeakPower;
import examplemod.character.MyCharacter;

public class Quanhuanbaozou extends CustomCard {
    public static final String ID = "leiheng:Quanhuanbaozou";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/quanhuanbaozou.png"; // 使用现有图片，你需要替换为合适的图片
    private static final int COST = 3;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int STR_LOSS = 5;
    private static final int WEAK_AMT = 2;
    private static final int DAMAGE_AMT = 10;
    private static final int ATTACK_TIMES = 4;
    private static final int BLOCK_AMT = 8;
    private static final int BLOCK_TIMES = 2;

    public Quanhuanbaozou() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.baseDamage = DAMAGE_AMT;
        this.damage = this.baseDamage;

        this.baseMagicNumber = this.magicNumber = STR_LOSS;
        
        this.baseBlock = BLOCK_AMT;
        this.block = this.baseBlock;

        this.exhaust = false; // 卡牌不会消耗
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(2);
            this.upgradeDamage(2);
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给予敌人 STR_LOSS 层力量降低
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                m,
                p,
                new com.megacrit.cardcrawl.powers.StrengthPower(m, -this.magicNumber),
                -this.magicNumber,
                true,
                AbstractGameAction.AttackEffect.NONE
        ));

        // 给予敌人 WEAK_AMT 层虚弱
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                m,
                p,
                new WeakPower(m, WEAK_AMT, false),
                WEAK_AMT
        ));

        // 造成 DAMAGE_AMT 伤害 ATTACK_TIMES 次
        for (int i = 0; i < ATTACK_TIMES; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT
            ));
        }

        // 获得 BLOCK_AMT 护甲 BLOCK_TIMES 次
        for (int i = 0; i < BLOCK_TIMES; i++) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(
                    p,
                    p,
                    BLOCK_AMT
            ));
        }
    }
}