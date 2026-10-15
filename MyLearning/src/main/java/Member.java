public class Member {
    private final String id;
    String name;
    int grade;

    public Member(){
        this("이름없음",1);
        System.out.println("기본 생성자 실행");
    }

    public Member(String name, int grade){
        System.out.println("전체 생성자 실행");
        this.id = "M-" + name;
        this.name = name;
        this.grade = grade;
    }
}
