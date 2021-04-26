local AI = {{id=1,name="普通近战战士",act="一旦有敌方进入警戒范围，则记录仇恨。一旦被攻击，则记录仇恨。一旦生命值低于15%，则逃跑。间隔一段时间放技能。",alertHate=100,beAttackedHate=100,dangerHP=30,runAwayHP=15,eviteEnemyNum=20,isSpecial=false,firstAttackTarget=_ATTACKTARGET_WARRIOR},
{id=2,name="普通治疗者",act="一旦警戒范围内有友军受伤，则产生治疗想法。一旦治疗范围内没有友军并且战场上有友军，则移动至该友军处。一旦生命值低于30%，则清除一切想法，产生自救想法。间隔一段时间放技能。",alertHate=100,beAttackedHate=0,dangerHP=75,runAwayHP=0,eviteEnemyNum=3,isSpecial=false,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=3,name="普通远程战士",act="一旦有敌方进入警戒范围，则记录仇恨。一旦被攻击，则记录仇恨。一旦被近身攻击，则后撤。一旦生命值低于20%，则逃跑。间隔一段时间放技能。",alertHate=100,beAttackedHate=100,dangerHP=55,runAwayHP=20,eviteEnemyNum=1,isSpecial=false,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=4,name="鲁莽型近战",act="一旦有敌方进入警戒范围，则记录仇恨。一旦被攻击，则记录高仇恨。技能一冷却就放技能。",alertHate=100,beAttackedHate=200,dangerHP=40,runAwayHP=0,eviteEnemyNum=20,isSpecial=false,firstAttackTarget=_ATTACKTARGET_MAGIAN},
{id=5,name="鸡贼型近战",act="一旦有敌方进入警戒范围，则记录仇恨。一旦被攻击，则记录仇恨。一旦被2名以上敌人围攻，则后撤。一旦生命值低于25%，则逃跑。技能一冷却就放技能。",alertHate=100,beAttackedHate=20,dangerHP=45,runAwayHP=25,eviteEnemyNum=2,isSpecial=false,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=6,name="暴力型远程战士",act="一旦有敌方进入警戒范围，则记录仇恨。一旦被攻击，则记录仇恨。技能一冷却就放技能。",alertHate=100,beAttackedHate=100,dangerHP=40,runAwayHP=6,eviteEnemyNum=5,isSpecial=false,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=7,name="无脑型战士",act="随机选择一个目标，除非目标死亡，否则不会变更目标，目标死亡后再次随机选择一个目标。技能一冷却就放技能。",alertHate=0,beAttackedHate=0,dangerHP=20,runAwayHP=0,eviteEnemyNum=100,isSpecial=true,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=8,name="捣乱型战士",act="每60秒清空所有想法，随机选择一个敌方单位记入仇恨。随机放技能。",alertHate=0,beAttackedHate=0,dangerHP=35,runAwayHP=20,eviteEnemyNum=100,isSpecial=true,firstAttackTarget=_ATTACKTARGET_RANDOM},
{id=9,name="高智商型战士",act="如果没有目标，则选择离自己近并且落单的敌方单位为目标，一旦攻击自己的敌人比自己强，则后撤，有技能就放。一旦生命值低于20%，则逃跑。",alertHate=0,beAttackedHate=20,dangerHP=35,runAwayHP=20,eviteEnemyNum=3,isSpecial=true,firstAttackTarget=_ATTACKTARGET_RANDOM}
}
return AI