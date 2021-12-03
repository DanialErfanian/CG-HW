function addPoint(name, point) {
  const db = JSON.parse(localStorage.getItem("db")) || {}
  if (!(name in db))
    db[name] = []

  db[name].push(point)

  localStorage.setItem("db", JSON.stringify(db))
  console.log(db)
}

function getPoints(name) {
  const db = JSON.parse(localStorage.getItem("db")) || {}
  if (!(name in db))
    return []
  return db[name];
}
