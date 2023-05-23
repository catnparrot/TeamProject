package Project;

import java.util.Scanner;

public class Main {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "12345";
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final double DEFAULT_BALANCE = 3000;

    private static UserDAO userDAO;
    private static ProductDAO productDAO;
    private static Scanner scanner;
    private static User currentUser;
    private static int loginAttempts;

    public static void main(String[] args) {
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
        scanner = new Scanner(System.in);
        loginAttempts = 0;

        connectToDatabase();

        System.out.println("프로그램에 오신 것을 환영합니다!");

        boolean programRunning = true;
        while (programRunning) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 0:
                    programRunning = false;
                    System.out.println("프로그램을 종료합니다.");
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    displayProductInventory();
                    break;
                case 3:
                    registerNewUser();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }

        closeDatabaseConnection();
        scanner.close();
    }

    private static void connectToDatabase() {
        // userDAO를 사용하여 데이터베이스에 연결합니다.
        // userDAO.setConnection(connection)을 사용하여 연결을 설정합니다.
    }

    private static void closeDatabaseConnection() {
        // userDAO를 사용하여 데이터베이스 연결을 종료합니다.
    }

    private static void displayMenu() {
        System.out.println("메인 메뉴:");
        System.out.println("1. 로그인");
        System.out.println("2. 재고 확인");
        System.out.println("3. 회원 가입");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); // 개행 문자를 소비합니다.
        return choice;
    }

    private static void login() {
        if (currentUser != null) {
            System.out.println("이미 로그인되어 있습니다. 현재 사용자: " + currentUser.getName());
            return;
        }

        System.out.print("사용자 이름을 입력하세요: ");
        String username = scanner.nextLine();

        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        User user = userDAO.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("로그인 성공!");
            currentUser = user;
            loginAttempts = 0;

            // 로그인한 사용자 메뉴 표시
            showLoggedInUserMenu();
        } else {
            System.out.println("로그인 실패. 잘못된 사용자 이름 또는 비밀번호입니다.");
            loginAttempts++;

            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                System.out.println("로그인 시도 횟수가 너무 많습니다. 프로그램을 종료합니다.");
                System.exit(0);
            }
        }
    }

    private static void showLoggedInUserMenu() {
        boolean loggedInMenuRunning = true;

        while (loggedInMenuRunning) {
            System.out.println("사용자 메뉴:");
            System.out.println("1. 물품 구매");
            System.out.println("2. 재고 확인");
            System.out.println("3. 로그아웃");
            System.out.print("선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자를 소비합니다.

            switch (choice) {
                case 1:
                    purchaseProduct();
                    break;
                case 2:
                    displayProductInventory();
                    break;
                case 3:
                    loggedInMenuRunning = false;
                    System.out.println("로그아웃되었습니다. 메인 메뉴로 돌아갑니다.");
                    currentUser = null;
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }

    private static void purchaseProduct() {
        displayProductInventory();

        System.out.print("물품 이름을 입력하세요: ");
        String productName = scanner.nextLine();

        System.out.print("수량을 입력하세요: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 개행 문자를 소비합니다.

        Product product = productDAO.getProductByName(productName);
        if (product != null) {
            double totalPrice = product.getPrice() * quantity;
            if (totalPrice <= currentUser.getBalance()) {
                // 사용자 계정에서 잔액 차감
                currentUser.setBalance(currentUser.getBalance() - totalPrice);
                userDAO.updateUser(currentUser);

                System.out.println("구매가 완료되었습니다!");
                System.out.println("총 가격: " + totalPrice);
                System.out.println("남은 잔액: " + currentUser.getBalance());
            } else {
                System.out.println("죄송합니다. 잔액이 부족합니다. 구매가 취소되었습니다.");
            }
        } else {
            System.out.println("물품을 찾을 수 없습니다. 구매가 취소되었습니다.");
        }
    }

    private static void displayProductInventory() {
        System.out.println("물품 재고:");
        System.out.println("-----------------");
        System.out.println("물품 이름\t가격");

        for (Product product : productDAO.getAllProducts()) {
            System.out.println(product.getName() + "\t\t" + product.getPrice());
        }

        System.out.println("-----------------");
    }

    private static void registerNewUser() {
        System.out.print("사용자 이름을 입력하세요: ");
        String username = scanner.nextLine();

        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password, DEFAULT_BALANCE);
        boolean registrationSuccess = userDAO.registerUser(newUser);

        if (registrationSuccess) {
            System.out.println("회원 가입이 완료되었습니다! 새로운 계정으로 로그인해주세요.");
        } else {
            System.out.println("회원 가입에 실패했습니다. 다시 시도해주세요.");
        }
    }
}
