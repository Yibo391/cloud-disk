import axios from 'axios'

const instance = axios.create({
    baseURL: 'http://localhost:9090',
    withCredentials: true, // Important for sending credentials
    timeout: 5000,
})

export default instance
