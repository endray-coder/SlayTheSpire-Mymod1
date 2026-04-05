package examplemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PreyPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = "leiheng:PreyPower";
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PreyPower(AbstractCreature owner) {
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
        PreyPower preyPower = (PreyPower) owner.getPower(POWER_ID);
        // 施加-5力量
        if(preyPower==null) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner,
                    owner,
                    new StrengthPower(owner, -5),
                    -5
            ));
        }
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    // 当能力被移除时，恢复力量
    @Override
    public void onRemove() {
        // 恢复5力量
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner,
                owner,
                new StrengthPower(owner, 5),
                5
        ));
    }
}