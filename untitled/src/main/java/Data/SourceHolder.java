package Data;

import java.util.Collection;
import java.util.List;

public class SourceHolder<A> {

	private List<A> streamSource;

	public SourceHolder() {
	}

	public SourceHolder(List<A> streamSource) {
		this.streamSource = streamSource;
	}

	public void setStreamSource(List<A> streamSource) {
		this.streamSource = streamSource;
	}

	public List<A> getStreamSource() {
		return streamSource;
	}
}
