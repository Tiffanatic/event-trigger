package gg.xp.xivsupport.events.triggers.easytriggers.conditions;

import gg.xp.xivsupport.events.actlines.events.HasTargetIndex;
import gg.xp.xivsupport.events.triggers.easytriggers.model.NumericOperator;
import gg.xp.xivsupport.events.triggers.easytriggers.model.SimpleCondition;

public class TargetCountFilter implements SimpleCondition<HasTargetIndex> {

	public NumericOperator operator = NumericOperator.EQ;
	@Description("Target Count (from 1)")
	public long expected;

	@Override
	public boolean test(HasTargetIndex event) {
		return operator.checkLong(event.getTargetIndex(), expected);
	}


	@Override
	public String fixedLabel() {
		return "Target Count";
	}

	@Override
	public String dynamicLabel() {
		return "Target Count " + operator.getFriendlyName() + ' ' + expected;
	}

	@Override
	public Class<HasTargetIndex> getEventType() {
		return HasTargetIndex.class;
	}
}
