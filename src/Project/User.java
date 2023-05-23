package Project;

class User {
    private String name;
    private String password;
    private double balance;

    public User(String name, String password, double balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

	public void setBalance(double d) {
		// TODO Auto-generated method stub
		
	}


}
