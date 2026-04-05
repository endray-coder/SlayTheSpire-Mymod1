package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.burn;

public class FanTheFlames extends CustomCard {
    public static final String ID = "Leiheng:FanTheFlames";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public FanTheFlames() {

        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust=false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 遍历所有敌人，将燃烧层数翻倍
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                // 获取敌人的燃烧能力
                burn burnPower = (burn) monster.getPower(burn.POWER_ID);
                if (burnPower != null && burnPower.amount > 0) {
                    // 计算新的燃烧层数（翻倍）
                    int newAmount = burnPower.amount * 2;
                    // 移除旧的燃烧能力
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                            monster,
                            p,
                            burnPower
                    ));
                    // 添加新的燃烧能力，层数为原来的两倍
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            monster,
                            p,
                            new burn(monster, newAmount),
                            newAmount
                    ));
                }
            }
        }
    }
}