import './App.css';
import axios from 'axios';
import React, {useEffect, useState} from 'react'

function App() {
    const logo = 'My First Kafka';

    let [data, setData] = useState([]);
    let [isDo, setDo] = useState(false);

    useEffect(() => {
        let interval = setInterval(() => {
            if (isDo) {
                getMessage();
            }
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
        const topic = 'test-topic';
        const url = 'http://localhost:8080/kafka/get?topic=' + topic;

        axios.get(url)
            .then((res) => {
                if (res.status === 200) {
                    let copy = [...data, ...res.data];
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
                    <h1>Producer</h1>
                    <input type={"text"} id='proInput' placeholder="input data" onKeyDown={(e) => onKeyPressEnter(e)}/>
                    <button onClick={() => sendMessage()}>전송</button>
                </div>
                <div className="right brick">
                    <h1>Consumer</h1>
                    <div className="pretty p-switch p-fill">
                        <input name="start_stat" type="checkbox" onChange={(e) => {setDo(e.target.checked);}}/>
                        <div className="state p-success">
                            <label>start</label>
                        </div>
                    </div>

                    <h2>DATA</h2>
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