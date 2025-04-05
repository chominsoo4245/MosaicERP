import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";

export default function Chart() {
    const data = [
        { date: '04-01', expected: 100000, actual: 98000 },
        { date: '04-02', expected: 95000, actual: 87000 },
        { date: '04-03', expected: 90000, actual: 91000 },
    ];

    return (
        <div className={"ml-4 mt-4 p-4"}>
            <LineChart width={400} height={300} data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />

                {/* 예상 금액 선 */}
                <Line type="monotone" dataKey="expected" name="예상 금액" stroke="#8884d8" />

                {/* 실제 금액 선 */}
                <Line type="monotone" dataKey="actual" name="실제 금액" stroke="#ff4d4f" />
            </LineChart>
        </div>
    );
}