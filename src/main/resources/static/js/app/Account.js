class Account {
    constructor() {
        this.$signinForm = $('#sign-in-form');
        this.$addUserButton = $('#add-user-button');
        this.$updateUserButton = $('.update-user-button');
        this.$deleteUserButton = $('.delete-user-button');
        this.$errorMessage = $('#errorMessage');
        this.requestUtil = new RequestUtil();

        this.addEvent();
    }

    addEvent() {
        this.$signinForm.on("submit", this.handleFormSubmit.bind(this));
        this.$addUserButton.addEventListener('click', function (e) {
           // TODO : 회원 추가 모달
        });
        this.$deleteUserButton.addEventListener('click', function (e) {
            if(confirm("탈퇴 처리 하시겠습니까?")) {
                this.withdraw();
            }
        });

    }

    async withdraw() {

        const id = $('#id').val();

        const response = await this.requestUtil.sendRequest('/rest/accounts/delete', 'POST', _data);
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

