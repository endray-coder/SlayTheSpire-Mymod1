package examplemod.modcore;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import examplemod.cards.Defend;
import examplemod.cards.Strike;
import examplemod.character.MyCharacter;
import com.badlogic.gdx.graphics.Color;

import static examplemod.character.MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;

@SpireInitializer
public class mymod1 implements EditCardsSubscriber, EditCharactersSubscriber, EditStringsSubscriber {
    private static final String MY_CHARACTER_BUTTON = "img/char/Character_Button.png";
    private static final String MY_CHARACTER_PORTRAIT = "img/char/Character_Portrait.png";
    private static final String BG_ATTACK_512 = "img/512/bg_attack_512.png";
    private static final String BG_POWER_512 = "img/512/bg_power_512.png";
    private static final String BG_SKILL_512 = "img/512/bg_skill_512.png";
    private static final String small_orb = "img/char/small_orb.png";
    private static final String BG_ATTACK_1024 = "img/1024/bg_attack.png";
    private static final String BG_POWER_1024 = "img/1024/bg_power.png";
    private static final String BG_SKILL_1024 = "img/1024/bg_skill.png";
    private static final String big_orb = "img/char/card_orb.png";
    private static final String energy_orb = "img/char/cost_orb.png";

    public static final Color MY_COLOR = new Color(27.0F / 255.0F, 120.0F / 255.0F, 120.0F / 255.0F, 1.0F);

    public mymod1() {
        BaseMod.addColor(EXAMPLE_GREEN, MY_COLOR, MY_COLOR, MY_COLOR,
                MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512,
                BG_SKILL_512, BG_POWER_512, energy_orb, BG_ATTACK_1024,
                BG_SKILL_1024, BG_POWER_1024, big_orb, small_orb
        );
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new mymod1();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        UnlockTracker.markCardAsSeen(Strike.ID);
        BaseMod.addCard(new Defend());
        UnlockTracker.markCardAsSeen(Defend.ID);
    }

    @Override
    public void receiveEditCharacters() {
        // 添加角色
        BaseMod.addCharacter(new MyCharacter(MyCharacter.PlayerColorEnum.MY_CHARACTER.name()),
                "img/char/Character_Button.png",
                "img/char/Character_Portrait.png",
                MyCharacter.PlayerColorEnum.MY_CHARACTER);
    }

    @Override
    public void receiveEditStrings() {

        // 加载卡牌本地化文件
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/zhs/cardStrings.json");
        // 加载角色本地化文件
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/zhs/characterStrings.json");
    }
}