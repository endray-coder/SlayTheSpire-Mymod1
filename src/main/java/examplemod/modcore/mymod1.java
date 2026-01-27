package examplemod.modcore;

// 1. 导入ModTheSpire核心注解
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
// 2. 导入BaseMod核心类和卡牌订阅接口
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import examplemod.cards.Strike;

@SpireInitializer
public class mymod1 implements EditCardsSubscriber {

    public mymod1() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new mymod1();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
    }
}