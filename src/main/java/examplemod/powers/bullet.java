package examplemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import examplemod.powers.BulletDrawPower;
import examplemod.relics.reload_relic;

public class bullet extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = "mymod:bullet";
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public bullet(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = "img/powers/Example84.png";
        String path48 = "img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    // 静态方法：消耗子弹并触发效果，返回实际消耗的子弹数量
    public static int consumeBullets(AbstractPlayer player, int amount) {
        bullet bulletPower = (bullet) player.getPower(POWER_ID);
        if (bulletPower != null && bulletPower.amount >= amount) {
            // 计算实际消耗的子弹数量
            int actualConsumed = Math.min(bulletPower.amount, amount);
            
            // 消耗子弹
            if (bulletPower.amount > actualConsumed) {
                bulletPower.amount -= actualConsumed;
                bulletPower.updateDescription();
            } else {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(player, player, bulletPower));
            }
            
            // 更新遗物的子弹消耗计数
            reload_relic.addBulletConsumed(actualConsumed);
            
            // 触发子弹消耗效果
            triggerBulletConsumedEffects(player, actualConsumed);
            return actualConsumed;
        }
        return 0;
    }

    // 触发子弹消耗效果
    private static void triggerBulletConsumedEffects(AbstractPlayer player, int times) {
        // 触发 BulletDrawPower
        BulletDrawPower bulletDrawPower = (BulletDrawPower) player.getPower(BulletDrawPower.POWER_ID);
        if (bulletDrawPower != null) {
            for (int i = 0; i < times; i++) {
                bulletDrawPower.onBulletConsumed();
            }
        }
        
        // 触发 BulletDefensePower
        BulletDefensePower bulletDefensePower = (BulletDefensePower) player.getPower(BulletDefensePower.POWER_ID);
        if (bulletDefensePower != null) {
            for (int i = 0; i < times; i++) {
                bulletDefensePower.onBulletConsumed();
            }
        }

    }
}