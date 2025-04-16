import React, { useState } from 'react';

// Mock 데이터: 창고와 각 창고의 계층적 구역 정보
const mockWarehouses = [
    {
        id: 1,
        name: '창고 A',
        zones: [
            { id: 101, name: '1층 - A 구역', coordinates: 'X:10, Y:20' },
            { id: 102, name: '1층 - B 구역', coordinates: 'X:15, Y:25' },
            { id: 103, name: '2층 - A 구역', coordinates: 'X:30, Y:40' },
            { id: 104, name: '2층 - B 구역', coordinates: 'X:35, Y:45' }
        ]
    },
    {
        id: 2,
        name: '창고 B',
        zones: [
            { id: 201, name: '1층 - 중앙 구역', coordinates: 'X:50, Y:60' },
            { id: 202, name: '2층 - 좌측 구역', coordinates: 'X:55, Y:65' }
        ]
    }
];

/**
 * WarehouseLocationSelector
 * - 사용자가 창고와 해당 창고 내 구역(zone)을 선택할 수 있는 UI를 제공합니다.
 * - onSelect 콜백을 통해 선택된 창고와 구역의 ID를 부모 컴포넌트에 전달합니다.
 */
function WarehouseLocationSelector({ onSelect }) {
    const [selectedWarehouseId, setSelectedWarehouseId] = useState('');
    const [selectedZoneId, setSelectedZoneId] = useState('');

    const handleWarehouseChange = (e) => {
        const warehouseId = e.target.value;
        setSelectedWarehouseId(warehouseId);
        setSelectedZoneId('');
        // 창고 선택 시 구역은 초기화: onSelect에서 구역은 null로 전달
        onSelect({ warehouseId, zoneId: null });
    };

    const handleZoneChange = (e) => {
        const zoneId = e.target.value;
        setSelectedZoneId(zoneId);
        onSelect({ warehouseId: selectedWarehouseId, zoneId });
    };

    // 선택된 창고 데이터 (없으면 빈 배열)
    const selectedWarehouse = mockWarehouses.find(
        (wh) => wh.id.toString() === selectedWarehouseId
    );

    return (
        <div className="p-4 border rounded bg-white">
            <div className="mb-4">
                <label className="block mb-1 font-semibold">창고 선택</label>
                <select
                    className="w-full p-2 border rounded"
                    value={selectedWarehouseId}
                    onChange={handleWarehouseChange}
                >
                    <option value="">-- 창고 선택 --</option>
                    {mockWarehouses.map((wh) => (
                        <option key={wh.id} value={wh.id}>
                            {wh.name}
                        </option>
                    ))}
                </select>
            </div>
            {selectedWarehouse && (
                <div className="mb-4">
                    <label className="block mb-1 font-semibold">구역 선택</label>
                    <select
                        className="w-full p-2 border rounded"
                        value={selectedZoneId}
                        onChange={handleZoneChange}
                    >
                        <option value="">-- 구역 선택 --</option>
                        {selectedWarehouse.zones.map((zone) => (
                            <option key={zone.id} value={zone.id}>
                                {zone.name}
                            </option>
                        ))}
                    </select>
                </div>
            )}
        </div>
    );
}

export default WarehouseLocationSelector;
