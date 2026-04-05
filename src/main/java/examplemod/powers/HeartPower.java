package examplemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import examplemod.relics.reload_relic;

public class HeartPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = "Leiheng:HeartPower";
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HeartPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 不可叠加的能力
        this.amount = -1;

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
        this.description = DESCRIPTIONS[0];
    }

    // 确保不会重复触发
    @Override
    public void onInitialApplication() {
        // 检查是否已经存在该能力
        HeartPower existingPower = (HeartPower) owner.getPower(POWER_ID);
        if (existingPower != null && existingPower != this) {
            // 如果已经存在，移除新的实例
            this.owner.powers.remove(this);
        } else {
            // 基础效果：给予5力量5敏捷
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner,
                    owner,
                    new StrengthPower(owner, 5),
                    5
            ));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner,
                    owner,
                    new DexterityPower(owner, 5),
                    5
            ));

        }
    }

    public void atStartOfTurn() {
        // 获取消耗的子弹数量
        int bulletCount = reload_relic.bulletConsumedCount;
        if (bulletCount > 0) {
            int bonus = bulletCount / 10;
            if (bonus > 0) {
                // 给予临时力量
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        owner,
                        owner,
                        new StrengthPower(owner, bonus),
                        bonus,
                        true
                ));
                // 给予临时敏捷
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        owner,
                        owner,
                        new DexterityPower(owner, bonus),
                        bonus,
                        true
                ));
            }
        }
    }
    // 当能力被移除时，失去5力量5敏捷
    @Override
    public void onRemove() {
        // 失去5力量
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner,
                owner,
                new StrengthPower(owner, -5),
                -5
        ));
        // 失去5敏捷
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner,
                owner,
                new DexterityPower(owner, -5),
                -5
        ));
    }
}