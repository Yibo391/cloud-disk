import axios from 'axios'

const instance = axios.create({
    baseURL: 'http://localhost:9090',
    withCredentials: true, // 如果使用了 Session，需要开启
    timeout: 10000,
})

export default instance
