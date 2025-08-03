package gg.xp.xivsupport.events.triggers.easytriggers.conditions;

import gg.xp.xivdata.data.*;
import gg.xp.xivsupport.events.actlines.events.TetherEvent;
import gg.xp.xivsupport.events.triggers.easytriggers.model.NumericOperator;
import gg.xp.xivsupport.events.triggers.easytriggers.model.SimpleCondition;

public class TetherIdFilter implements SimpleCondition<TetherEvent> {

	public NumericOperator operator = NumericOperator.EQ;
	@Description("Tether ID: ")
	public long expected;

	@Override
	public boolean test(TetherEvent tether) {
		return operator.checkLong(tether.getId(), expected);
	}


	@Override
	public String fixedLabel() {
		return "Tether ID";
	}

	@Override
	public String dynamicLabel() {
		return String.format("Tether ID %s 0x%x (%s)", operator.getFriendlyName(), expected, expected);
	}

	@Override
	public Class<TetherEvent> getEventType() {
		return TetherEvent.class;
	}
}
