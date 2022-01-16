import { apiServerUrl } from "./config";

export async function findTours(index, size, filters) {
  let filterQuery = '';
  for (const filter in filters) {
    filterQuery = filterQuery.concat(`&${filter}=${filters[filter]}`)
  }
  const response = await fetch(
    `${apiServerUrl}/tours?page=${index-1}&size=${size}${filterQuery}`);
  if (!response.ok) {
    throw response.statusText;
  }
  const totalCount = parseInt(response.headers.get("X-Total-Count"));
  const tours = await response.json();
  return { totalCount, tours };
}

export async function findTourById(id) {
  const response = await fetch(`${apiServerUrl}/tours/${id}`);
  if (!response.ok) {
    throw response.statusText;
  }
  const tour = await response.json();
  return tour;
}

export async function createTour(tour) {
  const response = await fetch(`${apiServerUrl}/tours`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: JSON.stringify(tour),
  });
  if (!response.ok) {
    throw response.statusText;
  }
}

export async function updateTour(tour) {
  const response = await fetch(`${apiServerUrl}/tours`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'PUT',
    body: JSON.stringify(tour),
  });
  if (!response.ok) {
    throw response.statusText;
  }
}

export async function deleteTourById(id) {
  const response = await fetch(`${apiServerUrl}/tours/${id}`, { method: 'DELETE' });
  if (!response.ok) {
    throw response.statusText;
  }
}
