package io.sporkpgm.filter;

public abstract class Condition implements Filter {

	private String name;
	private State state;

	public Condition(String name, State state) {
		this.name = name;
		this.state = state;
	}

	public abstract State match(FilterContext context);

	@Override
	public State matches(FilterContext context) {
		if(state.toBoolean()) {
			return match(context);
		} else {
			return match(context).reverse();
		}
	}

	public State getState() {
		return state;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Condition{state=" + state.toString() + "}";
	}

}