import axios from "axios";
import { useEffect, useState } from "react";

interface Office {
  id: number;
  name: string;
  officeNumber: number | string;
}

const API_URL = "/api/office";

function App() {
  const [offices, setOffices] = useState<Office[]>([]);
  const [search, setSearch] = useState("");
  const [message, setMessage] = useState("Loading offices...");

  // Get the offices from the backend when the page opens
  useEffect(() => {
    axios
      .get<Office[]>(API_URL)
      .then((response) => {
        setOffices(response.data);
        setMessage("");
      })
      .catch(() => {
        setMessage("Could not load offices.");
      });
  }, []);

  // Delete one office
  function handleDelete(office: Office) {
    const sure = window.confirm("Delete office " + office.name + "?");
    if (!sure) {
      return;
    }

    axios
      .delete(API_URL + "/" + office.id)
      .then(() => {
        // keep every office except the deleted one
        setOffices(offices.filter((item) => item.id !== office.id));
        setMessage("");
      })
      .catch(() => {
        setMessage("Could not delete the office.");
      });
  }

  // Keep only the offices that match the search text
  const searchText = search.toLowerCase();
  const filteredOffices = offices.filter((office) => {
    const nameMatches = String(office.name).toLowerCase().includes(searchText);
    const numberMatches = String(office.officeNumber).includes(searchText);
    return nameMatches || numberMatches;
  });

  return (
    <div className="mx-auto max-w-3xl px-4 py-8 font-sans text-slate-800">
      <p className="m-0 text-[13px] uppercase tracking-wider text-gray-500">
        Clinic administration
      </p>
      <h1 className="mb-2 mt-1 text-3xl font-bold">Offices</h1>
      <p className="mb-3 text-gray-600">
        Browse and manage the clinic's available offices.
      </p>

      <div className="rounded-lg border border-gray-200 bg-white p-5">
        <h2 className="mb-1 text-xl font-semibold">
          All offices ({offices.length})
        </h2>
        <p className="mb-3 text-gray-600">Search by office name or number.</p>

        <input
          className="mb-4 w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200"
          type="text"
          placeholder="Enter an office name or number"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />

        <table className="w-full border-collapse">
          <thead>
            <tr>
              {["Office name", "Office number", "Action"].map((heading) => (
                <th
                  key={heading}
                  className="border-b-2 border-gray-200 px-3 py-2 text-left text-[13px] font-semibold text-gray-500"
                >
                  {heading}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filteredOffices.map((office) => (
              <tr key={office.id} className="hover:bg-gray-50">
                <td className="border-b border-gray-100 px-3 py-2 font-semibold">
                  {office.name}
                </td>
                <td className="border-b border-gray-100 px-3 py-2">
                  {office.officeNumber}
                </td>
                <td className="border-b border-gray-100 px-3 py-2">
                  <button
                    className="mr-2 cursor-pointer rounded-md bg-red-600 px-3 py-1.5 text-white hover:bg-red-700"
                    onClick={() => handleDelete(office)}
                  >
                    Delete
                  </button>
                  <button
                    className="mr-2 cursor-not-allowed rounded-md bg-gray-400 px-3 py-1.5 text-white"
                    disabled
                    title="Update will be available in a future class"
                  >
                    Update
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filteredOffices.length === 0 && message === "" && (
          <p className="text-center text-gray-500">No offices found.</p>
        )}

        <p className="text-amber-700">{message}</p>
      </div>
    </div>
  );
}

export default App;
