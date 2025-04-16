import React from "react";

export default function Tabs({ activeTab, setActiveTab, tabs, children }) {
    return (
        <div>
            <div className="flex border-b">
                {tabs.map((tab) => (
                    <button
                        key={tab.id}
                        onClick={() => setActiveTab(tab.id)}
                        className={
                            "px-4 py-2 focus:outline-none " +
                            (activeTab === tab.id
                                ? "border-b-2 border-blue-500 font-bold text-blue-600"
                                : "text-gray-600")
                        }
                    >
                        {tab.label}
                    </button>
                ))}
            </div>

            <div className="mt-4">
                {children}
            </div>
        </div>
    );
}