import { apiServerUrl } from "./config";

export async function findRegions(index, size, filters) {
  let filterQuery = '';
  for (const filter in filters) {
    filterQuery = filterQuery.concat(`&${filter}=${filters[filter]}`)
  }
  const response = await fetch(
    `${apiServerUrl}/regions?page=${index-1}&size=${size}${filterQuery}`);
  if (!response.ok) {
    throw response.statusText;
  }
  const totalCount = parseInt(response.headers.get("X-Total-Count"));
  const regions = await response.json();
  return { totalCount, regions };
}

export async function findRegionById(id) {
  const response = await fetch(`${apiServerUrl}/regions/${id}`);
  if (!response.ok) {
    throw response.statusText;
  }
  const region = await response.json();
  return region;
}

export async function createRegion(region) {
  const response = await fetch(`${apiServerUrl}/regions`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: JSON.stringify(region),
  });
  if (!response.ok) {
    throw response.statusText;
  }
}

export async function updateRegion(region) {
  const response = await fetch(`${apiServerUrl}/regions`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'PUT',
    body: JSON.stringify(region),
  });
  if (!response.ok) {
    throw response.statusText;
  }
}

export async function deleteRegionById(id) {
  const response = await fetch(`${apiServerUrl}/regions/${id}`, { method: 'DELETE' });
  if (!response.ok) {
    throw response.statusText;
  }
}
