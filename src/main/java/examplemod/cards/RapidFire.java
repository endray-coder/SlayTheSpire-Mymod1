package examplemod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import examplemod.character.MyCharacter;
import examplemod.powers.bullet;
import examplemod.powers.burn;

import java.util.Random;

public class RapidFire extends CustomCard {
    public static final String ID = "Leiheng:RapidFire";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String IMG_PATH = "img/cards/Strike.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public RapidFire() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 2;
        this.baseMagicNumber = 1;
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
        // 获取当前子弹数量
        bullet bulletPower = (bullet) p.getPower(bullet.POWER_ID);
        if (bulletPower != null && bulletPower.amount > 0) {
            // 计算实际消耗的子弹数量（最多6颗）
            int bulletsToConsume = Math.min(bulletPower.amount, 6);
            
            // 随机对敌人造成伤害并施加燃烧
            Random random = new Random();
            for (int i = 0; i < bulletsToConsume; i++) {
                // 随机选择一个敌人
                AbstractMonster target = null;
                while (target == null || target.isDeadOrEscaped()) {
                    int randomIndex = random.nextInt(AbstractDungeon.getMonsters().monsters.size());
                    target = AbstractDungeon.getMonsters().monsters.get(randomIndex);
                }
                
                // 对随机敌人造成伤害
                AbstractDungeon.actionManager.addToBottom(new DamageAction(
                        target,
                        new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT
                ));
                
                // 对随机敌人施加燃烧
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        target,
                        p,
                        new burn(target, this.magicNumber),
                        this.magicNumber
                ));
            }
            
            // 消耗所有子弹
            bullet.consumeBullets(p, bulletPower.amount);
        }
    }
}