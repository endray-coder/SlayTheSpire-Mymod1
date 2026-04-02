
# 燃烧契约（burningpact）的消耗
this.addToBot(new ExhaustAction(1, false));
第二个参数是是否可以选择“零个”

# 抽牌
this.addToBot(new DrawCardAction(p, this.magicNumber));

# 祭品
## 扣血
this.addToBot(new LoseHPAction(p, p, 6));
## 获得能量
this.addToBot(new GainEnergyAction(2));

# 净化
是否随机，任意数字，canPickZero
this.addToBot(new ExhaustAction(this.magicNumber, false, true, true));

# 虚无
this.isEthereal = true;