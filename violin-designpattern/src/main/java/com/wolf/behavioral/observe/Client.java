package com.wolf.behavioral.observe;

/**
 * <b>功能</b>
 * Mapping subjects to their observers.However, such storage may be too expensive when there are many
 * subjects and few observers. One solution is to trade space for time by using an associative
 * look-up (e.g., a hash table)
 *
 * Who triggers the update?a.when subject's state is updated b.client after series of consecutive
 *
 * Dangling references to deleted subjects.
 *
 * todo:Avoiding observer-specific update protocols: the push and pull models.
 *
 * ChangeManager is an instance of the Mediator (273) pattern.
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class Client {

	public static void main(String[] args) {
		AutomaticClockSetting automaticClock = new AutomaticClockSetting();

		Observer digitalClock = new DigitalClock();
		Observer analogClock = new AnalogClock();

		automaticClock.attach(digitalClock);
		automaticClock.attach(analogClock);

		automaticClock.tick();

		ManualClockSetting manualClock = new ManualClockSetting();
		manualClock.attach(digitalClock);
		manualClock.attach(analogClock);

		manualClock.tick();
	}
}
