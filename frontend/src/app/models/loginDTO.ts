export class LoginDTO {
    email: string = "";
    password: string = "";

    LoginDTO(email: string, password: string) {
        this.email = email;
        this.password = password;
    }
}