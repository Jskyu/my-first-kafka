import './App.css';
import axios from 'axios';
import React, {useEffect, useState} from 'react'

function App() {
    const logo = 'My First Kafka';

    let [data, setData] = useState([]);

    useEffect(() => {
        let interval = setInterval(() => {
            getMessage();
        }, 3000);

        return () => {
            clearInterval(interval);
        }
    });

    const sendMessage = () => {
        const element = document.getElementById('proInput');
        const value = String(element.value);

        if(value === '') return;

        const url = 'http://localhost:8080/kafka/send';

        // 전송 API 콜
        axios.post(url, {
            message: value
        }, {
            withCredentials: true
        }).then((res) => {
            if (res.status === 200) { // 성공
                console.log('success send data. value = ' + value);
                element.value = '';
            }
        }).catch((res) => {
            console.log(res);
            alert('전송 실패');
        })
    };

    const getMessage = () => {
        console.log('get');
        const topic = 'test-topic';
        const url = 'http://localhost:8080/kafka/get?topic=' + topic;

        axios.get(url)
            .then((res) => {
                if (res.status === 200) {
                    let copy = [...data, res.data];
                    setData(copy);
                }
            })
            .catch((res) => {
                console.log(res);
            });
    }

    const onKeyPressEnter = (e) => {
        if(e.key === 'Enter') {
            sendMessage();
        }
    }

    return (
        <div className="App">
            <div className="black-nav">
                <h4>{logo}</h4>
            </div>
            <div className="contents">
                <div className="left brick">
                    <h1>producer</h1>
                    <input type={"text"} id='proInput' onKeyDown={(e) => onKeyPressEnter(e)}/>
                    <button onClick={() => sendMessage()}>전송</button>
                </div>
                <div className="right brick">
                    <h1>consumer</h1>

                    <h2>datas</h2>
                    <div className={'box'}>
                        {data.map((item, idx) => {
                            return (
                                <h4 key={idx}>{item}</h4>
                            );
                        })}
                    </div>
                </div>
            </div>
        </div>
    );
}
export default App;