package kr.co.anajo.component.auth;

import kr.co.anajo.component.member.Member;

/**
 * @author jodoyoung
 *
 */
public class Session {

	private String ip;

	private String time;

	enum Status {
		ACTIVE, TIMEOUT;
	}

	private Status status;

	private Member member;

	public Session(String ip, String time) {
		this.ip = ip;
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public String getTime() {
		return time;
	}

	public boolean isValid(String clientIp) {
		return isValidStatus() && isValidIp(clientIp);
	}

	public boolean isValidIp(String clientIp) {
		if (ip.equals(clientIp)) {
			return true;
		}
		return false;
	}

	public boolean isValidStatus() {
		if (status == Status.TIMEOUT) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [ip=" + ip + ", time=" + time + ", status=" + status + "]";
	}

}