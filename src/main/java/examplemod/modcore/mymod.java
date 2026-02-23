package examplemod.modcore;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import examplemod.cards.*;
import examplemod.character.MyCharacter;
import com.badlogic.gdx.graphics.Color;
import examplemod.relics.diejia;
import examplemod.relics.reload_relic;

import java.nio.charset.StandardCharsets;

import static examplemod.character.MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;

@SpireInitializer
public class mymod implements EditCardsSubscriber, EditCharactersSubscriber, EditStringsSubscriber, EditRelicsSubscriber , EditKeywordsSubscriber {
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

    public mymod() {
        BaseMod.addColor(EXAMPLE_GREEN, MY_COLOR, MY_COLOR, MY_COLOR,
                MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512,
                BG_SKILL_512, BG_POWER_512, energy_orb, BG_ATTACK_1024,
                BG_SKILL_1024, BG_POWER_1024, big_orb, small_orb
        );
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new mymod();
    }

    @Override
    public void receiveEditCards() {
        AutoAdd cards = new AutoAdd("mymod");
        cards.packageFilter("examplemod.cards").setDefaultSeen(false).any(basemod.abstracts.CustomCard.class, (info, card) -> {
            if (card != null) {
                BaseMod.addCard(card);
                if (info.seen) {
                    UnlockTracker.unlockCard(card.cardID);
                }
            }
        });
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
        String lang = "/zhs/";
        // 加载卡牌本地化文件
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization"+lang+"cardStrings.json");
        // 加载角色本地化文件
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization"+lang+"characterStrings.json");
        // 加载遗物本地化文件
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization"+lang+"relicStrings.json");

        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization"+lang+"Powers.json");
    }
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new diejia(), RelicType.SHARED); // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        BaseMod.addRelic(new reload_relic(), RelicType.SHARED);
    }
    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "zhs";


        String json = Gdx.files.internal("localization/" + lang + "/Keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("mymod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
                System.out.println("关键词注册成功：" + keyword.NAMES[0]);
            }
        }

    }
}