class SignIn {
    constructor() {
        this.$signinForm = $('#sign-in-form');
        this.$errorMessage = $('#errorMessage');
        this.requestUtil = new RequestUtil();

        this.addEvent();
    }

    addEvent() {
        this.$signinForm.on("submit", this.handleFormSubmit.bind(this));
    }

    async handleFormSubmit(event) {
        event.preventDefault();

        const id = $('#id').val();
        const password = $('#password').val();

        try {
            const _data = JSON.stringify({
                email: id,
                password: password
            });

            const response = await this.requestUtil.sendRequest('/api/auth/login', 'POST', _data);

        } catch (error) {
            this.showErrormessage("로그인을 실패했습니다.");
        }
    }

    showErrormessage(message) {
        this.$errorMessage.text(message);
        this.$errorMessage.removeClass("d-none");
    }
}

