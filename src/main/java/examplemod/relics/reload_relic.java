package examplemod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import examplemod.character.MyCharacter;
import examplemod.powers.bullet;
import examplemod.powers.overheat;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

public class reload_relic extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID =  "mymod:reload_relic";
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "img/UI/orb/layer1.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private boolean bulletsAdded = false;
    public reload_relic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new reload_relic();
    }

    // 重写canSpawn方法，确保只有特定角色能获得该遗物
    @Override
    public boolean canSpawn() {
        // 检查当前角色是否是我们的自定义角色
        return AbstractDungeon.player != null && AbstractDungeon.player.getClass() == MyCharacter.class;
    }
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        bulletsAdded = false;
    }

    @Override
    public void atTurnStart() {           //游戏似乎是等我这个函数跑完才会处理队列的实践
        super.atTurnStart();            //所以需要直接添加能力，不能addtobottom
        if (!bulletsAdded) {
            // 直接添加子弹到powers列表，确保立即生效
            bullet bulletPower = (bullet) AbstractDungeon.player.getPower(bullet.POWER_ID);
            if (bulletPower == null) {
                AbstractDungeon.player.powers.add(new bullet(AbstractDungeon.player, 12));
            } else if (bulletPower.amount <= 0) {
                bulletPower.amount = 12;
                bulletPower.updateDescription();
            }
            // 标记子弹已添加
            bulletsAdded = true;
        }
        // 检查玩家是否有子弹
        bullet bulletPower = (bullet) AbstractDungeon.player.getPower(bullet.POWER_ID);
        // 检查玩家是否有overheat能力作为标志
        overheat overheatPower = (overheat) AbstractDungeon.player.getPower(overheat.POWER_ID);

        if (bulletPower == null || bulletPower.amount <= 0) {
            // 没有子弹且overheat能力未存在
            if (overheatPower == null) {
                // 获得3层敏捷
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new DexterityPower(AbstractDungeon.player, 3),
                        3
                ));
                // 失去2层力量
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, -2),
                        -2
                ));
                // 添加overheat能力作为标志
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new overheat(AbstractDungeon.player)
                ));
            }
        } else {
            // 有子弹且overheat能力存在
            if (overheatPower != null) {
                // 移除敏捷
                AbstractPower dexterityPower = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
                if (dexterityPower != null && dexterityPower.amount >= 3) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new DexterityPower(AbstractDungeon.player, -3),
                            -3
                    ));
                }
                // 恢复力量
                AbstractPower strengthPower = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
                if (strengthPower != null && strengthPower.amount <= -2) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, 2),
                            2
                    ));
                }
                // 移除overheat能力标志
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        overheatPower
                ));
            }
        }
    }
}