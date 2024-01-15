class RequestUtil {
    constructor() {
    }

    async sendRequest(url, requestMethod, data) {
        const requestOptions = {
            method: requestMethod,
            headers: {
                'Content-Type': 'application/json',
            },
            body: data,
        };

        try {
            const response = await fetch(url, requestOptions);
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const result = await response.json();
            return result;
        } catch (error) {
            throw error;
        }
    }
}
